package com.panet.imeta.core.lifecycle;

import java.util.HashSet;
import java.util.Set;

import com.panet.imeta.core.logging.LogWriter;
import com.panet.imeta.core.util.ResolverUtil;

public class LifecycleSupport implements LifecycleListener
{
	private Set<LifecycleListener> lifeListeners;

	private static LogWriter log = LogWriter.getInstance();

	public LifecycleSupport()
	{
		ResolverUtil<LifecycleListener> listeners = new ResolverUtil<LifecycleListener>();
		listeners.find(new ResolverUtil.IsA(LifecycleListener.class), "com.panet.imeta.core.lifecycle.pdi");
		Set<Class<? extends LifecycleListener>> listenerClasses = listeners.getClasses();

		lifeListeners = new HashSet<LifecycleListener>(listenerClasses.size());

		for (Class<? extends LifecycleListener> clazz : listenerClasses)
		{
			try
			{
				lifeListeners.add(clazz.newInstance());
			} 
			catch (Throwable e)
			{
				log.logError("Spoon", "Unable to init listener:" + e.getMessage(), new Object[] {});
				continue;
			}
		}
	}

	public void onStart(LifeEventHandler handler) throws LifecycleException
	{
		for (LifecycleListener listener : lifeListeners)
			listener.onStart(handler);
	}

	public void onExit(LifeEventHandler handler) throws LifecycleException
	{
		for (LifecycleListener listener : lifeListeners)
			listener.onExit(handler);
	}
}
