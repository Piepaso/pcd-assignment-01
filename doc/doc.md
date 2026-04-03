# Report tecnico - Assignment 01 (`Poool`)

## 1. Analisi del problema concorrente

Il dominio applicativo combina tre componenti con vincoli differenti:

- simulazione fisica di molte entita (carico CPU-bound);
- interazione utente asincrona (eventi GUI non deterministici);
- rendering periodico su Event Dispatch Thread (vincoli di thread-affinity).

Dal punto di vista concorrente, i requisiti critici sono:

- **coerenza dello stato fisico**: collisioni e aggiornamenti devono essere calcolati su uno stato consistente della board;
- **reattivita I/O**: input utente e bot non devono bloccare il loop fisico;
- **separazione dei ruoli**: i thread CPU-bound devono evitare dipendenze dirette da GUI/EDT;
- **scalabilita**: throughput crescente all'aumentare del numero di core, almeno per workload con molte palline.

## 2. Architettura adottata

L'architettura e organizzata come insieme di agenti concorrenti che interagiscono tramite monitor espliciti.

### 2.1 Agenti

- **`BallsAgent`** (CPU-bound): esegue integrazione del moto, applicazione dei comandi e risoluzione collisioni sul proprio partizionamento di palline.
- **`ViewAgent`** (I/O-bound): acquisisce snapshot consistenti della board, aggiorna il `ViewModel` e coordina il ritmo di rendering con EDT.
- **`ActiveController`** (I/O-bound): serializza gli input provenienti dalla GUI e li converte in comandi di kick.
- **`BotAgent`** (sleep/I/O-bound): genera comandi periodici per i player controllati automaticamente.

Il partizionamento delle palline sui thread fisici e statico per frame:

`from_i = i * N / T`, `to_i = (i + 1) * N / T`,

dove `N` e il numero totale di palline e `T` il numero di thread di fisica.

### 2.2 Monitor

- **`BallsMonitor`**: monitor principale con due barriere cicliche:
  - barriera 1: sincronizza la fase di update cinematico;
  - barriera 2: sincronizza la fase di collisione e produce snapshot condivisibile (`BoardData`).
- **`CmdMonitor`**: mailbox concorrente dei kick (`Map<playerId, Kick>`), con accesso sincronizzato e semantica consume-once.
- **`RenderMonitor`**: handshake tra `ViewAgent` e EDT per evitare rendering sovrapposti.

## 3. Modello di comportamento (livello astratto)

Il comportamento globale puo essere descritto con una rete di Petri a due cicli accoppiati.

### 3.1 Ciclo fisico

**Posti**: `FrameStart`, `UpdatedByAllAgents`, `CollisionsByAllAgents`, `SnapshotReady`.

**Transizioni**:

- `T_update`: ciascun `BallsAgent` aggiorna stato e applica eventuale kick;
- `T_barrier_update`: scatta quando tutti gli agenti hanno completato `T_update`;
- `T_collision`: ciascun agente risolve collisioni sullo snapshot condiviso;
- `T_barrier_collision`: scatta quando tutti gli agenti hanno completato `T_collision`;
- `T_snapshot`: generazione `BoardData` consistente.

### 3.2 Ciclo view/input

**Posti**: `InputEvents`, `KickAvailable`, `BoardDataAvailable`, `RenderInProgress`, `RenderDone`.

**Transizioni**:

- `T_input`: `ActiveController` converte evento GUI in comando;
- `T_consumeKick`: il thread fisico consuma il comando del player corrispondente;
- `T_renderRequest`: `ViewAgent` richiede repaint su EDT;
- `T_renderAck`: EDT segnala completamento render.

L'accoppiamento tra i due cicli avviene tramite `BoardDataAvailable` e `KickAvailable`.

## 4. Scelte di sincronizzazione e correttezza

### 4.1 Proprieta di safety

- nessuna lettura parziale dello stato durante collisioni (uso di barriera);
- un kick per player e consumato al massimo una volta (remove atomica in `CmdMonitor`);
- il rendering legge solo snapshot pubblicate dopo la sincronizzazione dei thread fisici.

### 4.2 Proprieta di liveness

- assenza di starvation fra `BallsAgent` in condizioni nominali: tutte le barriere sono globali e cicliche;
- `ViewAgent` non forza il rallentamento del motore fisico: attende solo frame nuovi.

## 5. Valutazione prestazionale

### 5.1 Setup sperimentale

- CPU: Intel i5-13600KF (20 core logici: 6 P-core HT + 8 E-core);
- metrica: iterazioni/secondo del motore fisico;
- confronto: versione concorrente (`T=20`) vs baseline single-thread (`T=1`).

### 5.2 Risultati

Con circa 4500 palline:

- `T=20`: ~50-60 iterazioni/s;
- `T=1`: ~6 iterazioni/s.

Speedup osservato: `S = T1 / Tp ~= 10` (ordine di grandezza).

Con 50 palline:

- `T=1`: fino a ~60000 iterazioni/s;
- `T=20`: non oltre ~8000 iterazioni/s.

Punto di pareggio empirico: ~150 palline.

### 5.3 Discussione

I risultati mostrano due regimi:

- **workload grande**: il parallelismo compensa l'overhead di sincronizzazione e migliora il throughput;
- **workload piccolo**: domina l'overhead (barriere, lock, cache-coherence), per cui il single-thread risulta superiore.

La scelta runtime `T=1` sotto soglia e `T=availableProcessors` sopra soglia e quindi coerente con i dati sperimentali.

## 6. Verifica con model checking (JPF)

### 6.1 Obiettivo

Verificare assenza di violazioni di sicurezza su componenti critici di sincronizzazione, in particolare `CmdMonitor` e protocollo di barriere di `BallsMonitor`.

### 6.2 Proprieta candidate

- **P1 (consume-once)**: un kick inserito per un player non puo essere consumato due volte;
- **P2 (no missed publication)**: ogni snapshot pubblicata rimane osservabile da `ViewAgent` almeno una volta;
- **P3 (barrier progress)**: in assenza di interruzioni, i thread fisici non restano bloccati indefinitamente tra due frame.

### 6.3 Strategia

- costruzione di harness ridotti (2-3 thread fisici, poche palline, scheduling non deterministico);
- esplorazione interleaving con JPF su monitor isolati;
- uso di assert su invarianti di stato (`threadsUpdated`, `threadsFinished`, disponibilita kick).

## 7. Limiti e trade-off

- l'uso di thread platform e semplice e diretto, ma non ottimizza automaticamente il bilanciamento dinamico del carico;
- il doppio punto di barriera massimizza la coerenza del frame, ma introduce latenza di sincronizzazione;
- il vantaggio del parallelismo dipende fortemente dalla cardinalita delle palline e dall'hardware eterogeneo (P-core/E-core).

La variante task-based con Executor Framework puo ridurre parte della complessita di gestione thread e migliorare adattivita del scheduling, mantenendo invariati i vincoli di consistenza del modello fisico.
