import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.Comparator;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.RandomAccess;
import java.util.AbstractList;
import java.io.Writer;
import java.util.List;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.math.BigInteger;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Stack;
import java.util.HashSet;

public class HopcroftKarp 
{
	public static void main(String[] args) 
	{
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		OutputWriter out = new OutputWriter(outputStream);
		MaximumMatching solver = new MaximumMatching();
		int T = 1;
		for(int count = 0; count < T; count++)
		{
			solver.solve(count+1, in, out);			
		}
		out.close();

	}//end of main()
}

class MaximumMatching 
{
	public static int N = 2001;//2000 nodes + 1 NIL node
	LinkedList<Integer> adjList[];

	public void solve(int testNumber, InputReader in, OutputWriter out) 
	{
		adjList = (LinkedList<Integer>[])new LinkedList[N];
		for(int count = 1; count < N; count++)
		{
			adjList[count] = new LinkedList<Integer>();
		}

		int M = in.readInt();
		int x, y;
		for(int count = 0; count < M; count++)		
		{
			x = in.readInt();
			y = in.readInt();
			
			adjList[x].add(y);
			adjList[y].add(x);
		}

		Graph g = new Graph(adjList);
		g.hopcroftKarp();

		out.print(g.matching + "\n");

	}//end of solve()

	public static void print(LinkedList<Integer> aList[])
	{
		for(int count1 = 0; count1 < aList.length; count1++)
		{
			System.out.print(count1);
			for(int count2 = 0; count2 < aList[count1].size(); count2++)
			{
				System.out.print("->"+aList[count1].get(count2));
			}
			System.out.println();
		}
	}

}//end of class MaximumMatching

/*
 * http://en.wikipedia.org/wiki/Hopcroft%E2%80%93Karp_algorithm
 */
class Graph
{
	public static int INF = Integer.MAX_VALUE;
	public static int N = MaximumMatching.N;
	public static int NIL = 0;

	int match[];
	int dist[];
	LinkedList<Integer> adjList[];
	int matching;

	public Graph(LinkedList<Integer> adjList)
	{
		match = new int[N];
		dist  = new int[N];
		this.adjList = adjList;
	}

	public int hopcroftKarp()
	{
		matching = 0;
		
		for(int i = 0; i < N; i++)
		{
			match[i] = NIL;
		}

		while(bfs())
		{
			for(int i = 1; i < N; i++)
			{
				if(match[i] == NIL && dfs(i))
				{
					matching++;
				}
			}
		}

		return matching;
	}

	private boolean bfs()
	{
		int i, u, v, len;
		Queue<Integer> q = new LinkedList<Integer>();

		for(i = 1; i < N; i++)
		{
			if(match[i] == NIL)
			{
				dist[i] = 0;
				q.add(i);
			}
			else
			{
				dist[i] = INF;
			}
		}

		dist[NIL] = INF;

		while(q.size() != 0)
		{
			u = q.poll();
			if(u != NIL)
			{
				len = adjList[u].size();
				
				for(i = 0; i < len; i++)
				{
					v = adjList[u].get(i);
					if(dist[match[v]] == INF) 
					{
						dist[match[v]] = dist[u] + 1;
						q.add(match[v]);
					}
				}
			}
		}

		return (dist[NIL] != INF);
	}

	private boolean dfs(int u)
	{
		int i, v, len;
		if(u != NIL)
		{
			len = adjList[u].size();

			for(i = 0; i < len; i++)
			{
				v = adjList[u].get(i);
				if(dist[match[v]] == dist[u] + 1)
				{
					if(dfs(match[v]))
					{
						match[u] = v;
						match[v] = u;
						return true;
					}
				}

			}
		
			dist[u] = INF;
			return false;

		}
		
		return true;
	}

}//end of class Graph

class InputReader 
{
	private InputStream stream;
	private byte[] buf = new byte[1024];
	private int curChar;
	private int numChars;
 
	public InputReader(InputStream stream) 
	{
		this.stream = stream;
	}
	 
	public int read() 
	{
		if (numChars == -1)
			throw new InputMismatchException();
		if (curChar >= numChars) 
		{
			curChar = 0;
			try 
			{
				numChars = stream.read(buf);
			} 
			catch (IOException e) 
			{
				throw new InputMismatchException();
			}

			if (numChars <= 0)
				return -1;
		}
		return buf[curChar++];
	}
 
	public int readInt() 
	{
		int c = read();

		while (isSpaceChar(c))
			c = read();

		int sgn = 1;
		if (c == '-') 
		{
			sgn = -1;
			c = read();
		}

		int res = 0;
		do 
		{
			if (c < '0' || c > '9')
				throw new InputMismatchException();
			res *= 10;
			res += c - '0';
			c = read();
		} 
		while (!isSpaceChar(c));

		return res * sgn;
	}

	public String readString() 
	{
		StringBuilder sb = new StringBuilder();
		int c = read();
		while (isWhiteSpace(c)) 
		{
		    c = read();
		}

		while (!isWhiteSpace(c)) 
		{
		    sb.appendCodePoint(c);
		    c = read();
		}

		return sb.toString();
	}

	public static boolean isWhiteSpace(int c) 
	{
		return c >= -1 && c <= 32;
	}
 
	public static boolean isSpaceChar(int c) 
	{
		return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
	}
 
}
 
class OutputWriter 
{
	private final PrintWriter writer;
	 
	public OutputWriter(OutputStream outputStream) 
	{
		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
	}
	 
	public OutputWriter(Writer writer) 
	{
		this.writer = new PrintWriter(writer);
	}
	 
	public void print(Object...objects) 
	{
		for (int i = 0; i < objects.length; i++) 
		{
			if (i != 0)
				writer.print(' ');
			writer.print(objects[i]);
		}
	}
	 
	public void printLine(Object...objects) 
	{
		print(objects);
		writer.println();
	}
	 
	public void close() 
	{
		writer.close();
	}
 
}
