# ingeniuous-mcts
Java implementation of the board game ingenious with various Monte Carlo Tree Search versions and enhancements

Description of the game: [here](https://en.wikipedia.org/wiki/Ingenious_(board_game))

Monte Carlo Tree Search (MCTS): http://mcts.ai/pubs/mcts-survey-master.pdf

Progressive bias: Augmenting a node's UCT value with a term depending on a light heuristic that loses significance as more information on the node becomes available. See: http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.106.3015&rep=rep1&type=pdf

Move groups: Moves on a given depth of the search tree are pooled into a small number of group nodes based on their similarity. The idea is that information gathered about one of its representatives will translate to all other moves of the respective node.
See: http://www.csse.uwa.edu.au/cig08/Proceedings/papers/8057.pdf
and https://dke.maastrichtuniversity.nl/m.winands/documents/SaitoCGW2007.pdf

