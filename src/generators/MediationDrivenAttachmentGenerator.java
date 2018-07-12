/*
 * Copyright 2006 - 2015
 *     Stefan Balev     <stefan.balev@graphstream-project.org>
 *     Julien Baudry    <julien.baudry@graphstream-project.org>
 *     Antoine Dutot    <antoine.dutot@graphstream-project.org>
 *     Yoann Pigné      <yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin   <guilhelm.savin@graphstream-project.org>
 * 
 * This file is part of GraphStream <http://graphstream-project.org>.
 * 
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 * 
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */
package generators;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.graph.Node;
import org.util.set.Array;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MediationDrivenAttachmentGenerator extends BaseGenerator {
	/**
	 * Degree of each node.
	 */
	protected ArrayList<Integer> degrees;

	/**
	 * The maximum number of links created when a new node is added.
	 */
	protected int maxLinksPerStep;

	/**
	 * Does the generator generates exactly {@link #maxLinksPerStep}.
	 */
	protected boolean exactlyMaxLinksPerStep = false;
	
	/**
	 * The sum of degrees of all nodes
	 */
	protected int sumDeg;
	
	/**
	 * The sum of degrees of nodes not connected to the new node
	 */
	protected int sumDegRemaining;
	
	/**
	 * Set of indices of nodes connected to the new node
	 */
	protected Set<Integer> connected;

	/**
	 * New generator.
	 */
	public MediationDrivenAttachmentGenerator() {
		this(1, false);
	}

	public MediationDrivenAttachmentGenerator(int maxLinksPerStep) {
		this(maxLinksPerStep, false);
	}

	public MediationDrivenAttachmentGenerator(int maxLinksPerStep,
											  boolean exactlyMaxLinksPerStep) {
		this.directed = false;
		this.maxLinksPerStep = maxLinksPerStep;
		this.exactlyMaxLinksPerStep = exactlyMaxLinksPerStep;
	}

	/**
	 * Maximum number of edges created when a new node is added.
	 * 
	 * @return The maximum number of links per step.
	 */
	public int getMaxLinksPerStep() {
		return maxLinksPerStep;
	}

	/**
	 * True if the generator produce exactly {@link #getMaxLinksPerStep()}, else
	 * it produce a random number of links ranging between 1 and
	 * {@link #getMaxLinksPerStep()}.
	 * 
	 * @return Does the generator generates exactly
	 *         {@link #getMaxLinksPerStep()}.
	 */
	public boolean produceExactlyMaxLinkPerStep() {
		return exactlyMaxLinksPerStep;
	}

	/**
	 * Set how many edge (maximum) to create for each new node added.
	 * 
	 * @param max
	 *            The new maximum, it must be strictly greater than zero.
	 */
	public void setMaxLinksPerStep(int max) {
		maxLinksPerStep = max > 0 ? max : 1;
	}

	/**
	 * Set if the generator produce exactly {@link #getMaxLinksPerStep()}
	 * (true), else it produce a random number of links ranging between 1 and
	 * {@link #getMaxLinksPerStep()} (false).
	 * 
	 * @param on
	 *            Does the generator generates exactly
	 *            {@link #getMaxLinksPerStep()}.
	 */
	public void setExactlyMaxLinksPerStep(boolean on) {
		exactlyMaxLinksPerStep = on;
	}

	/**
	 * Start the generator. Two nodes connected by edge are added.
	 * 
	 * @see org.graphstream.algorithm.generator.Generator#begin()
	 */
	public void begin() {
		addNode("0");
		addNode("1");
		addEdge("0_1", "0", "1");
		degrees = new ArrayList<Integer>();
		degrees.add(1);
		degrees.add(1);
		sumDeg = 2;
		connected = new HashSet<Integer>();
	}

	/**
	 * Step of the generator. Add a node and try to connect it with some others.
	 * 
	 * The number of links is randomly chosen between 1 and the maximum number
	 * of links per step specified in {@link #setMaxLinksPerStep(int)}.
	 * 
	 * The complexity of this method is O(n) with n the number of nodes if the
	 * number of edges created per new node is 1, else it is O(nm) with m the
	 * number of edges generated per node.
	 * 
	 * @see org.graphstream.algorithm.generator.Generator#nextEvents()
	 */
	public boolean nextEvents() {
		// Generate a new node.
		int nodeCount = degrees.size();
		String newId = nodeCount + "";
		addNode(newId);

		// Attach to how many existing nodes?
		int n = maxLinksPerStep;
		if (!exactlyMaxLinksPerStep)
			n = random.nextInt(n) + 1;
		n = Math.min(n, nodeCount);

		// Choose the nodes to attach to.
		sumDegRemaining = sumDeg;
		for (int i = 0; i < n; i++)
			chooseAnotherNode();
		
		for (int i : connected) {
			addEdge(newId + "_" + i, newId, i + "");
			degrees.set(i, degrees.get(i) + 1);
		}
		connected.clear();
		degrees.add(n);
		sumDeg += 2 * n;

		// It is always possible to add an element.
		return true;
	}
	
	/**
	 * Choose randomly one of the remaining nodes 
	 */
	protected void chooseAnotherNode() {
		int r = random.nextInt(sumDegRemaining);
		int runningSum = 0;
		int i = 0;
		while (runningSum <= r) {
			if (!connected.contains(i))
				runningSum += degrees.get(i);
			i++;
		}
		i--;
		ArrayList<Node> neighbours = new ArrayList<>();
		if (internalGraph != null) {
			Node rNode = internalGraph.getNode(i);
			Iterator<Node> iterator = rNode.getNeighborNodeIterator();
			while (iterator.hasNext()) {
				neighbours.add(iterator.next());
			}
			int winner = random.nextInt(neighbours.size());
			Node node = internalGraph.getNode(winner);
			int degree = node.getDegree();
			connected.add(node.getIndex());
			sumDegRemaining -= degree;
		} else {
			connected.add(i);
			sumDegRemaining -= degrees.get(i);
		}
	}


	/**
	 * Clean degrees.
	 * 
	 * @see org.graphstream.algorithm.generator.Generator#end()
	 */
	@Override
	public void end() {
		degrees.clear();
		degrees = null;
		connected = null;
		super.end();
	}
}