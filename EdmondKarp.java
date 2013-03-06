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

public class EdmondKarp 
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
	int N;
	int C[][];
	LinkedList<Integer> adjList[];

	public void solve(int testNumber, InputReader in, OutputWriter out) 
	{
		N = 2003;//2000 nodes + source + sink
		C = new int[N][N];
		adjList = (LinkedList<Integer>[])new LinkedList[N];

		for(int count = 1; count < N; count++)
		{
			adjList[count] = new LinkedList<Integer>();
		}

		int M = in.readInt();
		int x, y;
		for(int count = 0; count < M; count++)		
		{
			x = in.readInt()+1;
			y = in.readInt()+1;
			C[x][y] = 1;
			adjList[x].add(y);
		}

		//add edges from source to L
		for(int count = 2; count <= 1001; count++)
		{
			C[1][count] = 1;
			adjList[1].add(count);
		}

		//add edges from R to sink
		for(int count = 1002; count <= 2001; count++)
		{
			C[count][2002] = 1;
			adjList[count].add(2002);
		}

		Graph g = new Graph(N, C, adjList, 1, 2002);
		g.getMaxFlow();

		out.print(g.getMaxFlow() + "\n");

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
 * http://en.wikipedia.org/wiki/Edmonds%E2%80%93Karp_algorithm
 */
class Graph
{
	int N;
	int C[][];
	int s;
	int t;
	int maxFlow;
	int flow[][];
	LinkedList<Integer> adjList[];
	boolean mark[];

	public Graph(int n, int C[][], LinkedList<Integer> adjList[], int s, int t)
	{
		N = n;
		flow = new int[N][N];
		maxFlow = 0;
		mark = new boolean[N];
		this.C = C;
		this.adjList = adjList;
		this.s = s;
		this.t = t;
	}

	public int getMaxFlow()
	{
		while(true)
		{
			int dx = BFS();	
			if(dx == 0)
				break;

			maxFlow = maxFlow + dx;
		}
		
		return maxFlow;
	}

	private int BFS()
	{
		int[] P = new int[N];
		Arrays.fill(P, -1);
		P[s] = s;

		int[] M = new int[N];
		M[s] = Integer.MAX_VALUE;

		Queue<Integer> q = new LinkedList<Integer>();
		q.offer(s);

		int u = -1;
		int v = -1;
		boolean isDone = false;

		while(!q.isEmpty())
		{
			u = q.poll();

			for(int count = 1; count <= adjList[u].size(); count++)
			{
				v = adjList[u].get(count - 1);

				if(C[u][v] - flow[u][v] > 0 && P[v] == -1)
				{
					P[v] = u;
					M[v] = Math.min(M[u], C[u][v] - flow[u][v]);
					if(v != t)
					{
						q.offer(v);
					}
					else
					{						
						while(P[v] != v)
						{
							u = P[v];
							flow[u][v] = flow[u][v] + M[t];
							flow[v][u] = flow[v][u] - M[t];
							v = u;
						}
						isDone = true;
						break;
					}
				}
			}

			if(isDone)
			{
				break;	
			}

		}//while (!q.isEmpty())

		if (P[t] == -1) 
		{	
			// We did not find a path to t
		        int sum = 0;
		        return sum;
		}

		return M[t];
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
