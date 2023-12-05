package Metro;

import java.util.*;

import javax.swing.JTextArea;

import java.io.*;

	
	public class Graph_M 
	{
		public class Vertex 
		{
			HashMap<String, Integer> nbrs = new HashMap<>();
		}

		static HashMap<String, Vertex> vtces;

		public Graph_M() 
		{
			vtces = new HashMap<>();
		}

		public int numVetex() 
		{
			return this.vtces.size();
		}

		public boolean containsVertex(String vname) 
		{
			return this.vtces.containsKey(vname);
		}

		public void addVertex(String vname) 
		{
			Vertex vtx = new Vertex();
			vtces.put(vname, vtx);
		}

		public void removeVertex(String vname) 
		{
			Vertex vtx = vtces.get(vname);
			ArrayList<String> keys = new ArrayList<>(vtx.nbrs.keySet());

			for (String key : keys) 
			{
				Vertex nbrVtx = vtces.get(key);
				nbrVtx.nbrs.remove(vname);
			}

			vtces.remove(vname);
		}

		public int numEdges() 
		{
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int count = 0;

			for (String key : keys) 
			{
				Vertex vtx = vtces.get(key);
				count = count + vtx.nbrs.size();
			}

			return count / 2;
		}

		public boolean containsEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return false;
			}

			return true;
		}

		public void addEdge(String vname1, String vname2, int value) 
		{
			Vertex vtx1 = vtces.get(vname1); 
			Vertex vtx2 = vtces.get(vname2); 

			if (vtx1 == null || vtx2 == null || vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.put(vname2, value);
			vtx2.nbrs.put(vname1, value);
		}

		public void removeEdge(String vname1, String vname2) 
		{
			Vertex vtx1 = vtces.get(vname1);
			Vertex vtx2 = vtces.get(vname2);
			
			//check if the vertices given or the edge between these vertices exist or not
			if (vtx1 == null || vtx2 == null || !vtx1.nbrs.containsKey(vname2)) {
				return;
			}

			vtx1.nbrs.remove(vname2);
			vtx2.nbrs.remove(vname1);
		}

		public void display_Map(JTextArea textArea) {
    StringBuilder mapStringBuilder = new StringBuilder();
    mapStringBuilder.append("\t Delhi Metro Map");
    mapStringBuilder.append("\t------------------");
    mapStringBuilder.append("----------------------------------------------------\n");

    ArrayList<String> keys = new ArrayList<>(vtces.keySet());

    for (String key : keys) {
        String str = key + " =>\n";
        Vertex vtx = vtces.get(key);
        ArrayList<String> vtxnbrs = new ArrayList<>(vtx.nbrs.keySet());

        for (String nbr : vtxnbrs) {
            str = str + "\t" + nbr + "\t";
            if (nbr.length() < 16)
                str = str + "\t";
            if (nbr.length() < 8)
                str = str + "\t";
            str = str + vtx.nbrs.get(nbr) + "\n";
        }
        mapStringBuilder.append(str);
    }

    mapStringBuilder.append("\t------------------");
    mapStringBuilder.append("---------------------------------------------------\n");

    // Set the text of the JTextArea
    textArea.setText(mapStringBuilder.toString());
}

		
		public void display_Stations() 
		{
			System.out.println("\n***********************************************************************\n");
			ArrayList<String> keys = new ArrayList<>(vtces.keySet());
			int i=1;
			for(String key : keys) 
			{
				System.out.println(i + ". " + key);
				i++;
			}
			System.out.println("\n***********************************************************************\n");
		}
			
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public boolean hasPath(String vname1, String vname2, HashMap<String, Boolean> processed) 
		{
			// DIR EDGE
			if (containsEdge(vname1, vname2)) {
				return true;
			}

			//MARK AS DONE
			processed.put(vname1, true);

			Vertex vtx = vtces.get(vname1);
			ArrayList<String> nbrs = new ArrayList<>(vtx.nbrs.keySet());

			//TRAVERSE THE NBRS OF THE VERTEX
			for (String nbr : nbrs) 
			{

				if (!processed.containsKey(nbr))
					if (hasPath(nbr, vname2, processed))
						return true;
			}

			return false;
		}
		
		
		private class DijkstraPair implements Comparable<DijkstraPair> 
		{
			String vname;
			String psf;
			int cost;

			/*
			The compareTo method is defined in Java.lang.Comparable.
			Here, we override the method because the conventional compareTo method
			is used to compare strings,integers and other primitive data types. But
			here in this case, we intend to compare two objects of DijkstraPair class.
			*/ 

			/*
			Removing the overriden method gives us this errror:
			The type Graph_M.DijkstraPair must implement the inherited abstract method Comparable<Graph_M.DijkstraPair>.compareTo(Graph_M.DijkstraPair)

			This is because DijkstraPair is not an abstract class and implements Comparable interface which has an abstract 
			method compareTo. In order to make our class concrete(a class which provides implementation for all its methods)
			we have to override the method compareTo
			 */
			@Override
			public int compareTo(DijkstraPair o) 
			{
				return o.cost - this.cost;
			}
		}
		
		public int dijkstra(String src, String des, boolean nan) 
		{
			int val = 0;
			ArrayList<String> ans = new ArrayList<>();
			HashMap<String, DijkstraPair> map = new HashMap<>();

			Heap<DijkstraPair> heap = new Heap<>();

			for (String key : vtces.keySet()) 
			{
				DijkstraPair np = new DijkstraPair();
				np.vname = key;
				//np.psf = "";
				np.cost = Integer.MAX_VALUE;

				if (key.equals(src)) 
				{
					np.cost = 0;
					np.psf = key;
				}

				heap.add(np);
				map.put(key, np);
			}

			//keep removing the pairs while heap is not empty
			while (!heap.isEmpty()) 
			{
				DijkstraPair rp = heap.remove();
				
				if(rp.vname.equals(des))
				{
					val = rp.cost;
					break;
				}
				
				map.remove(rp.vname);

				ans.add(rp.vname);
				
				Vertex v = vtces.get(rp.vname);
				for (String nbr : v.nbrs.keySet()) 
				{
					if (map.containsKey(nbr)) 
					{
						int oc = map.get(nbr).cost;
						Vertex k = vtces.get(rp.vname);
						int nc;
						if(nan)
							nc = rp.cost + 120 + 40*k.nbrs.get(nbr);
						else
							nc = rp.cost + k.nbrs.get(nbr);

						if (nc < oc) 
						{
							DijkstraPair gp = map.get(nbr);
							gp.psf = rp.psf + nbr;
							gp.cost = nc;

							heap.updatePriority(gp);
						}
					}
				}
			}
			return val;
		}
		
		private class Pair {
		    String vname;
		    String psf;
		    int min_dis;
		    int min_time;

		    public Pair(String vname, String psf, int min_dis, int min_time) {
		        this.vname = vname;
		        this.psf = psf;
		        this.min_dis = min_dis;
		        this.min_time = min_time;
		    }
		}

		public String Get_Minimum_Distance(String src, String dst) {
		    int min = Integer.MAX_VALUE;
		    String ans = "";
		    HashMap<String, Boolean> processed = new HashMap<>();
		    LinkedList<Pair> stack = new LinkedList<>();

		    // create a new pair
		    Pair sp = new Pair(src, src + "  ", 0, 0);
		    stack.addFirst(sp);

		    // while stack is not empty keep on doing the work
		    while (!stack.isEmpty()) {
		        // remove a pair from stack
		        Pair rp = stack.removeFirst();

		        if (rp.vname == null) {
		            // Skip processing if the vertex name is null
		            continue;
		        }

		        if (processed.containsKey(rp.vname)) {
		            continue;
		        }

		        // processed put
		        processed.put(rp.vname, true);

		        // if there exists a direct edge between removed pair and destination vertex
		        if (rp.vname.equals(dst)) {
		            int temp = rp.min_dis;
		            if (temp < min) {
		                ans = rp.psf;
		                min = temp;
		            }
		            continue;
		        }

		        Vertex rpvtx = vtces.get(rp.vname);

		        if (rpvtx == null || rpvtx.nbrs == null) {
		            // Skip processing if the vertex or its neighbors are null
		            continue;
		        }

		        ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

		        for (String nbr : nbrs) {
		            // process only unprocessed nbrs
		            if (!processed.containsKey(nbr)) {
		                // make a new pair of nbr and put in stack
		                Pair np = new Pair(nbr, rp.psf + nbr + "  ", rp.min_dis + rpvtx.nbrs.get(nbr), rp.min_time + 120 + 40 * rpvtx.nbrs.get(nbr));
		                stack.addFirst(np);
		            }
		        }
		    }
		    ans = ans + Integer.toString(min);
		    return ans;
		}

		public String Get_Minimum_Time(String src, String dst) {
		    int min = Integer.MAX_VALUE;
		    String ans = "";
		    HashMap<String, Boolean> processed = new HashMap<>();
		    LinkedList<Pair> stack = new LinkedList<>();

		    // create a new pair
		    Pair sp = new Pair(ans, ans, min, min);
		    sp.vname = src;
		    sp.psf = src + "  ";
		    sp.min_dis = 0;
		    sp.min_time = 0;

		    // put the new pair in queue
		    stack.addFirst(sp);

		    // while queue is not empty keep on doing the work
		    while (!stack.isEmpty()) {

		        // remove a pair from queue
		        Pair rp = stack.removeFirst();

		        if (rp.vname == null) {
		            // Skip processing if the vertex name is null
		            continue;
		        }

		        if (processed.containsKey(rp.vname)) {
		            continue;
		        }

		        // processed put
		        processed.put(rp.vname, true);

		        //if there exists a direct edge b/w removed pair and destination vertex
		        if (rp.vname.equals(dst)) {
		            int temp = rp.min_time;
		            if (temp < min) {
		                ans = rp.psf;
		                min = temp;
		            }
		            continue;
		        }

		        Vertex rpvtx = vtces.get(rp.vname);

		        if (rpvtx == null || rpvtx.nbrs == null) {
		            // Skip processing if the vertex or its neighbors are null
		            continue;
		        }

		        ArrayList<String> nbrs = new ArrayList<>(rpvtx.nbrs.keySet());

		        for (String nbr : nbrs) {
		            // process only unprocessed nbrs
		            if (!processed.containsKey(nbr)) {

		                // make a new pair of nbr and put in queue
		                Pair np = new Pair(nbr, nbr, min, min);
		                np.vname = nbr;
		                np.psf = rp.psf + nbr + "  ";
		                // np.min_dis = rp.min_dis + rpvtx.nbrs.get(nbr);
		                np.min_time = rp.min_time + 120 + 40 * rpvtx.nbrs.get(nbr);
		                stack.addFirst(np);
		            }
		        }
		    }
		    Double minutes = Math.ceil((double) min / 60);
		    ans = ans + Double.toString(minutes);
		    return ans;
		}

		
		public ArrayList<String> get_Interchanges(String str)
		{
			ArrayList<String> arr = new ArrayList<>();
			String res[] = str.split("  ");
			arr.add(res[0]);
			int count = 0;
			for(int i=1;i<res.length-1;i++)
			{
				int index = res[i].indexOf('~');
				String s = res[i].substring(index+1);
				
				if(s.length()==2)
				{
					String prev = res[i-1].substring(res[i-1].indexOf('~')+1);
					String next = res[i+1].substring(res[i+1].indexOf('~')+1);
					
					if(prev.equals(next)) 
					{
						arr.add(res[i]);
					}
					else
					{
						arr.add(res[i]+" ==> "+res[i+1]);
						i++;
						count++;
					}
				}
				else
				{
					arr.add(res[i]);
				}
			}
			arr.add(Integer.toString(count));
			arr.add(res[res.length-1]);
			return arr;
		}
		
		public static void Create_Metro_Map(Graph_M g)
		{
			g.addVertex("Clifton~B");
            g.addVertex("Gulshan-e-Iqbal~B");
            g.addVertex("Saddar~B");
            g.addVertex("Tariq Road~BY");
            g.addVertex("Karimabad~B");
            g.addVertex("Nazimabad~B");
            g.addVertex("Liaquatabad~BO");
            g.addVertex("Sohrab Goth~B");
            g.addVertex("Tower~Y");
            g.addVertex("Defence~Y");
            g.addVertex("University Road~Y");
            g.addVertex("Sadar~Y");
            g.addVertex("Karachi Cantt~YO");
            g.addVertex("Jinnah Hospital~Y");
            g.addVertex("Karachi Stadium~O");
            g.addVertex("Karachi University~O");
            g.addVertex("Jinnah International Airport~O");
            g.addVertex("North Nazimabad~BP");
            g.addVertex("Gulberg~PR");
            g.addVertex("Shahrah-e-Faisal~P");

         g.addEdge("Clifton~B", "Gulshan-e-Iqbal~B", 8);
         g.addEdge("Gulshan-e-Iqbal~B", "Saddar~B", 10);
         g.addEdge("Saddar~B", "Karimabad~B", 8);
         g.addEdge("Saddar~B", "Tariq Road~BY", 6); 
         g.addEdge("Tariq Road~BY", "Nazimabad~B", 9);
         g.addEdge("Nazimabad~B", "Liaquatabad~BO", 7);
         g.addEdge("Liaquatabad~BO", "Sohrab Goth~B", 6);
         g.addEdge("Tower~Y", "Defence~Y", 15);
        g.addEdge("Defence~Y", "Jinnah Hospital~Y", 6);
        g.addEdge("Jinnah Hospital~Y", "Tariq Road~BY", 7);
        g.addEdge("Tariq Road~BY", "Karachi Cantt~YO", 1);
        g.addEdge("Karachi Cantt~YO", "Sadar~Y", 2); 
        g.addEdge("Sadar~Y", "University Road~Y", 5);
        g.addEdge("Karachi Cantt~YO", "Karachi Stadium~O", 2);
        g.addEdge("Karachi Stadium~O", "Karachi University~O", 7);
        g.addEdge("Karachi University~O", "Jinnah International Airport~O", 8);
        g.addEdge("Nazimabad~B", "North Nazimabad~BP", 2); 
       g.addEdge("Shahrah-e-Faisal~P", "North Nazimabad~BP", 2);
          g.addEdge("Shahrah-e-Faisal~P", "Gulberg~PR", 3);

		}
		
					}
