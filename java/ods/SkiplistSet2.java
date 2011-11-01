package ods;

public class SkiplistSet2<T> extends SkiplistSSet<T> {
	protected int pickHeight() {
		int i = 0;
		while (i < 32 && rand.nextDouble() <= 1./Math.E)
			i++;
		return i;
	}

}
