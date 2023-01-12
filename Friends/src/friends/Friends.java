package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) 
	{
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		Queue<Person> q = new Queue<>();
		boolean[] rVisited = new boolean[g.members.length];
		int[] pVisited = new int[g.members.length];
		Person goal = g.members[g.map.get(p2)];
		boolean f = false;
		for(int i = 0; i < pVisited.length; i++)
		{
			pVisited[i] = -9999;
		}
		q.enqueue(g.members[g.map.get(p1)]);
		while(!q.isEmpty())
		{	
			Person current = q.dequeue();
			if(current == goal)
			{
				f = true;
			}
			rVisited[g.map.get(current.name)] = true;
			Friend friendPointer = g.members[g.map.get(current.name)].first;
			while(friendPointer != null)
			{
				if(!rVisited[friendPointer.fnum])
				{
					q.enqueue(g.members[friendPointer.fnum]);
					pVisited[friendPointer.fnum] = g.map.get(current.name);	
					rVisited[friendPointer.fnum] = true;
				}
				friendPointer = friendPointer.next;
			}
		}
		if(!f)
		{
			return null;
		}
		Stack<String> sta = new Stack<>();
		int prev = pVisited[g.map.get(goal.name)];
		sta.push(goal.name);
		while(prev != -9999)
		{
			sta.push(g.members[prev].name);
			prev = pVisited[prev];
		}
		ArrayList<String> arr = new ArrayList<>();
		while(!sta.isEmpty())
		{
			arr.add(sta.pop());
		}
		return arr;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) 
	{
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		boolean[] rVisited = new boolean[g.members.length];
		for (int i=0; i<rVisited.length; i++)
		{
			rVisited[i]= false;
		}
		ArrayList<ArrayList<String>> answer = new ArrayList<ArrayList<String>>();
		for (int i=0; i<rVisited.length; i++)
		{
			if (!rVisited[i]) 
			{
				rVisited[i]=!rVisited[i];
				if (g.members[i].school!=null && g.members[i].school.equalsIgnoreCase(school))
					answer.add(cliqueHelper(g.members[i].name,rVisited, g, school)); 
			
			}
		}
		if (answer.isEmpty())
		{
			return null;
		}
		return answer;
	}
	private static ArrayList<String> cliqueHelper (String p1, boolean[] rVisited, Graph g, String school) 
	{ 
		rVisited[g.map.get(p1)] = true;
		ArrayList<String> answer = new ArrayList<String>();
		answer.add(p1);
		if (g.members[g.map.get(p1)].first!=null)
		{
			for (Friend ptr = g.members[g.map.get(p1)].first; ptr!=null; ptr = ptr.next)
			{
				if (rVisited[ptr.fnum])
				{
					continue;
				}
				rVisited[ptr.fnum] = true;
				if (g.members[ptr.fnum].school!=null && g.members[ptr.fnum].school.equalsIgnoreCase(school))
				{
					answer.add(g.members[ptr.fnum].name);
					ArrayList<String> a = cliqueHelper(g.members[ptr.fnum].name, rVisited, g, school);
					if (a!=null)
					{
						for (int i=0; i< a.size(); i++)
						{
							if(!answer.contains(a.get(i)))
							{
								answer.add(a.get(i));
							}
						}
					}
				}
			}
		}
		if (answer.isEmpty())
		{
			return null;
		}
		return answer;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) 
	{
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<String> retValue = new ArrayList<>();
		boolean[] rVisited = new boolean[g.members.length];
		for(int i = 0; i < g.members.length; i++)
		{
			if(!rVisited[i])
			{
				connectorsHelper(g,rVisited, i, new int[g.members.length], new int[g.members.length], i, retValue, new boolean[g.members.length], i);
			}
		}
		return retValue;
	}

	private static void connectorsHelper(Graph g, boolean[] rVisited, int current, int[] fsnums, int backs[], int previous, ArrayList<String> cons, boolean[] backed, int start)
	{
		if(!rVisited[current])
		{
		
			rVisited[current] = true;
			fsnums[current] = fsnums[previous] + 1;
			backs[current] = fsnums[current];
			Friend friendPointer = g.members[current].first;
			while(friendPointer != null)
			{	
				if(!rVisited[friendPointer.fnum])
				{

					connectorsHelper(g, rVisited, friendPointer.fnum, fsnums, backs, current, cons, backed, start);
					if(fsnums[current] <= backs[friendPointer.fnum] && !cons.contains(g.members[current].name) && (current != start || backed[current]))
					{
							cons.add(g.members[current].name);
					}
					if(fsnums[current] > backs[friendPointer.fnum])
					{
						backs[current] = Math.min(backs[current], backs[friendPointer.fnum]);
					}
					backed[current] = true;
				}
				else
				{
					backs[current] = Math.min(backs[current], fsnums[friendPointer.fnum]);
				}
				friendPointer = friendPointer.next;
			}
		}
	}
}

