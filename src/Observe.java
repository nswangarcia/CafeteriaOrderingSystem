public interface Observe {

	public abstract void registerObserver(Observer observer);

	public abstract void unregisterObserver(Observer observer);

	public abstract void nofityObservers();

}
