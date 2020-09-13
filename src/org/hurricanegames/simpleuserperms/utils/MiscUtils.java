package org.hurricanegames.simpleuserperms.utils;

import java.util.Comparator;
import java.util.List;

public class MiscUtils {

	public static List<String> sort(List<String> list) {
		list.sort(DefaultComparator.<String>getInstance());
		return list;
	}

	public static final class DefaultComparator<E extends Comparable<E>> implements Comparator<E> {
		@SuppressWarnings("rawtypes")
		private static final DefaultComparator<?> INSTANCE = new DefaultComparator();

		public static <T extends Comparable<T>> Comparator<T> getInstance() {
			@SuppressWarnings("unchecked")
			Comparator<T> result = (Comparator<T>) INSTANCE;
			return result;
		}

		private DefaultComparator() {
		}

		@Override
		public int compare(E o1, E o2) {
			if (o1 == o2) {
				return 0;
			}
			if (o1 == null) {
				return 1;
			}
			if (o2 == null) {
				return -1;
			}
			return o1.compareTo(o2);
		}

	}

}
