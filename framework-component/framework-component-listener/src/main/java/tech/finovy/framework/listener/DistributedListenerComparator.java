package tech.finovy.framework.listener;

import java.util.Comparator;

public class DistributedListenerComparator implements Comparator<DistributedListener> {
    @Override
    public int compare(DistributedListener first, DistributedListener sen) {
        return Integer.compare(first.getOrder(),sen.getOrder());
    }
}
