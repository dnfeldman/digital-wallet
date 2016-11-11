# digital-wallet

### File Structure
All logic is under `src/main/scala/digitalwallet` directory.

`DigitalWallet` - main entry to the app. Contains logic to load initial graph and then stream new transactions through feature labelers and update graph with new relationships.

`labelers/*` contains classes that implement logic behind labeling transactions as 'trusted' or 'unverified' (Feature 1, 2, and 3 as described in the problem statement).

`processors/*` contains logic to stream and verify transactions from source files.

`modules/*` contains classes that deal with storing and interacting with graphs.

Unit tests for those classes are located in `src/test/scala/digitalwallet/*`.


### Design Considerations

- Relations between nodes in the `Graph` are stored in adjacency sets. I ended up using `IntMap` as the underlying structure behind `Graph`s. `IntMap` is Scala's implementation of FastMergeableIntegerMaps which makes modifying `IntMap`s a little more efficient. Since a lot of resources are used to update the graph after every transaction, switching from regular `HashMap[Int, Set[Int]]` actually resulted in ~20% speedup when runnning on full dataset (~6m records). That said, even though the processing time went down to about ~15 min, looking at GC/JVM Heap while the code is running seems to suggest there should be a way to speed this up even further.

- For figuring out if two nodes are within N degrees of each other, I used a bi-directional BFS algorithm to reduce memory footprint/improve speed. It's implemented in `DegreeOfSeparationChecker`.
