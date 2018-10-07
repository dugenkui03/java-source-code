package concurrent.locks;

/**
 * A synchronizer that may be exclusively owned by a thread.
 * <p>某个线程独占式拥有的同步器</p>
 * This class provides a basis for creating locks and related synchronizers
 * that may entail a notion of ownership.
 * <p>这个类提供了创建锁和相关同步器的基础——锁和同步器往往含有“所有权”的含义</p>
 * The {@code AbstractOwnableSynchronizer} class itself does not manage or
 * use this information. However, subclasses and tools may use
 * appropriately maintained values to help control and monitor access
 * and provide diagnostics.
 * <p>此类本身并不管理或者使用这些信息，但是他的子类和其他工具类可以通过维护适当的值来帮助控制和监视访问权，并且提供diagnostics诊断</p>
 *
 */
public abstract class AbstractOwnableSynchronizer
        implements java.io.Serializable {

    /** Use serial ID even though all fields transient. */
    private static final long serialVersionUID = 3737899427754241961L;

    /**
     * Empty constructor for use by subclasses.
     *
     */
    protected AbstractOwnableSynchronizer() { }

    /**
     * The current owner of exclusive mode synchronization.
     * <p>独占模式同步器 的当前拥有者（线程）——加锁时只有一个线程可以拥有锁，即独占模式</p>
     * transient 表示对象的此变量不会参与序列化(序列化是保存对象的信息，因此静态变量也不会被序列化)
     */
    private transient Thread exclusiveOwnerThread;

    /**
     * Sets the thread that currently owns exclusive access.
     * A {@code null} argument indicates that no thread owns access.
     * <p>设置当前拥有独占访问权限的线程，null表示没有线程拥有访问权限</p>
     *
     * This method does not otherwise impose any synchronization or
     * {@code volatile} field accesses.
     *
     * @param thread the owner thread
     */
    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    /**
     * Returns the thread last set by {@code setExclusiveOwnerThread},
     * or {@code null} if never set.  This method does not otherwise
     * <p>获取exclusiveOwnerThread引用的对象，即当前拥有独占访问权限的线程</p>
     * impose any synchronization or {@code volatile} field accesses.
     * @return the owner thread
     */
    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
