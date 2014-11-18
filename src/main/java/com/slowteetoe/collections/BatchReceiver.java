package com.slowteetoe.collections;

import java.util.List;

public interface BatchReceiver {
	
	void receiveBatch(List<?> objects);

}
