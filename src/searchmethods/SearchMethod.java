package searchmethods;

import agent.Problem;
import agent.Solution;

public interface SearchMethod {

   Solution search(Problem problem);
   
   Statistics getStatistics();
   
   void stop();
   
   boolean hasBeenStopped();
}