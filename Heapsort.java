/* 
 * Vincent Cozzo
 * A class to manage a Heapsort in Java
 * 
 * */
import java.util.*;

public class Heapsort<T extends Comparable<T>> {
	public Heapsort() {

	}

	private ArrayList<T> heapify(ArrayList<T> in) {
		int index = in.size() - 1;
		if ((index % 2) == 1) {
			/* make one initial comparison beforehand, then
			 * decrement index to make sure that index begins the
			 * routine as an even integer.
			 * */
			T lastEle = in.get(index);
			int parentInd = ((index - 1) / 2);
			T parent = in.get(parentInd);
			if ((lastEle.compareTo(parent)) < 0) {
				/* swap the child and parent Objects */
				T tmp = parent;
				in.set(parentInd, lastEle);
				in.set(index, tmp);
			}
			index --;
		}
		while (index > 0) {
			int childIndex;
			T lesserChild;
			if (in.get(index-1).compareTo(in.get(index)) < 0) {
				lesserChild = in.get(index-1);
				childIndex = index-1;
			} else {
				lesserChild = in.get(index);
				childIndex = index;
			}
			int parentInd = ((index - 1) / 2);
			T parent = in.get(parentInd);
			if ((lesserChild.compareTo(parent)) < 0) {
				/* swap the child and parent Objects */
				T tmp = parent;
				in.set(parentInd, lesserChild);
				in.set(childIndex, tmp);
				/* NOTE: so that I don't forget!!!
				 * There is an important step in here, 
				 * where you keep sifting down the element
				 * if it is larger than its children
				 * it's not as simple as "do one swap"
				 * so remember to keep checking!
				 * * */
				in = siftDown(in, childIndex);
			}
			index -= 2;
		}
		System.out.println("Heapify'd version is " + in.toString());
		return in;
	}

	private ArrayList<T> siftDown(ArrayList<T> preSift, int startInd) {
		int size = preSift.size();
		int currentInd = startInd;
		int leftInd = (2*startInd) + 1;
		int rightInd = (2*startInd) + 2;
		while ((rightInd < size) && (preSift.get(rightInd) != null)) {
			/*
			 * Get the minimum of the two children
			 * and compare to the current node
			 *  
			 * */
			T lesserChild;
			int nextCurrent;
			if (preSift.get(leftInd).compareTo(preSift.get(rightInd)) < 0) {
				lesserChild = preSift.get(leftInd);
				nextCurrent = leftInd;
			} else {
				lesserChild = preSift.get(rightInd);
				nextCurrent = rightInd;
			}

			if (lesserChild.compareTo(preSift.get(currentInd)) < 0) {
				/* swap the relevant data entries */
				T tmp = preSift.get(currentInd);
				preSift.set(currentInd, lesserChild);
				preSift.set(nextCurrent, tmp);
				/* and continue sifting down      */
				currentInd = nextCurrent;
				leftInd = ((2*currentInd) + 1);
				rightInd = ((2*currentInd) + 2);
				System.out.println("Now continuing to sift down using " + currentInd + " and the daughter node indices " + leftInd + " / " + rightInd);
			} else {
				/* this will stop sifting, as it should.   */
				rightInd = size;
				leftInd = size;
			}
		}
		if ((leftInd < size) && (preSift.get(leftInd) != null)) {
			T lesserChild = preSift.get(leftInd);
			int nextCurrent = leftInd;
			if (lesserChild.compareTo(preSift.get(currentInd)) < 0) {
				/* swap the relevant data entries */
				T tmp = preSift.get(currentInd);
				preSift.set(currentInd, lesserChild);
				preSift.set(nextCurrent, tmp);
			}
		}
		return preSift;
	}

	public ArrayList<T> heapsort(ArrayList<T> input) {
		int size = input.size();

		ArrayList<T> inter = heapify(input);
		ArrayList<T> result = new ArrayList<T>(size);
		for (int ind = 0; ind < size; ind ++) {
			result.add(null);
		}
		while (size > 0) {
			T tmp = inter.get(0);
			inter.set(0, inter.get(size-1));
			inter.set(size-1, null);
			result.set(size-1, tmp);
			inter = siftDown(inter, 0);
			size--;
		}
		return result;
	}

	public static void main(String[] args) {
		Heapsort<Integer> manager = new Heapsort<Integer>();
		ArrayList<Integer> testData = new ArrayList<Integer>();
		ArrayList<Integer> result;

		testData.add(new Integer(3));
		testData.add(new Integer(0));
		testData.add(new Integer(223));
		testData.add(new Integer(21));
		testData.add(new Integer(28));
		testData.add(new Integer(-3));
		testData.add(new Integer(23));
		testData.add(new Integer(40));
		testData.add(new Integer(26));
		testData.add(new Integer(39));

		System.out.println(testData.toString());

		result = manager.heapsort(testData);
		System.out.println(result.toString());
	}
}
