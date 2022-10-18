package com.micker.core.adapter;

import androidx.recyclerview.widget.DiffUtil;
import com.micker.helper.TLog;

public class AdapterDiffUtil {
    public static boolean calculateDiff(DiffUtil.Callback cb) {
        boolean isNeedNotifyDataSetChange = false;
        final int oldSize = cb.getOldListSize();
        final int newSize = cb.getNewListSize();
        if (newSize > 0) {
            int changedCount = 0;
            for (int i = 0; i < newSize; i++) {
                if (i < oldSize) {
                    if (!cb.areItemsTheSame(i, i)) {
                        changedCount++;
                    } else if (!cb.areContentsTheSame(i, i)) {
                        changedCount++;
                    }
                } else {
                    changedCount = changedCount + (newSize - i);
                    break;
                }
            }

            float rate = (changedCount * 1.0f) / newSize;
            isNeedNotifyDataSetChange = (rate >= 0.7);
            TLog.i("AdapterDataDelegate  newSize = " + newSize + "; changeCount = " + changedCount + "; rate = " + rate);
        }
        return isNeedNotifyDataSetChange;
    }
}
