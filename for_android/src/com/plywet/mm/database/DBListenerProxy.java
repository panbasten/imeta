package com.plywet.mm.database;

public class DBListenerProxy {

	
	private final MMListenerProvider provider = new DBListenerProvider(this);
	
	public final void dealAll(){
		this.provider.addObject("*");
		this.provider.deal();
	}
	
	public final void addListener(DBListenerInterface lis){
		this.provider.addListener(lis);
	}
	
	public final void removeListener(DBListenerInterface lis){
		this.provider.removeListener(lis);
	}
	
	public final void deal(String obj){
		this.provider.addObject(obj);
		this.provider.deal();
	}
}
