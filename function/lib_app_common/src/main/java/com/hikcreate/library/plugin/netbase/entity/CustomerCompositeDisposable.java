package com.hikcreate.library.plugin.netbase.entity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.OpenHashSet;

/**
 * 自定义disposable管理容器
 *
 * @author yslei
 * @date 2019/5/15
 * @email leiyongsheng@hikcreate.com
 */
public class CustomerCompositeDisposable implements Disposable, DisposableContainer {

    private OpenHashSet<Disposable> resources;

    private volatile boolean disposed;

    /**
     * Creates an empty CompositeDisposable.
     */
    public CustomerCompositeDisposable() {
    }

    /**
     * Creates a CompositeDisposables with the given array of initial elements.
     *
     * @param resources the array of Disposables to start with
     */
    public CustomerCompositeDisposable(Disposable... resources) {
        ObjectHelper.requireNonNull(resources, "resources is null");
        this.resources = new OpenHashSet<>(resources.length + 1);
        for (Disposable d : resources) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    /**
     * Creates a CompositeDisposables with the given Iterable sequence of initial elements.
     *
     * @param resources the Iterable sequence of Disposables to start with
     */
    public CustomerCompositeDisposable(Iterable<? extends Disposable> resources) {
        ObjectHelper.requireNonNull(resources, "resources is null");
        this.resources = new OpenHashSet<>();
        for (Disposable d : resources) {
            ObjectHelper.requireNonNull(d, "Disposable item is null");
            this.resources.add(d);
        }
    }

    public synchronized boolean isCurrentAllDisposed() {
        if (resources != null) {
            Object[] array = resources.keys();
            OpenHashSet<Disposable> set = new OpenHashSet<>();
            for (Object o : array) {
                if (o instanceof Disposable) {
                    if (((Disposable) o).isDisposed()) {
                        set.add((Disposable) o);
                    }
                }
            }
            if (set.size() == resources.size()) {
                clear();
                return true;
            } else {
                dispose(set);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public synchronized void dispose() {
        if (disposed) {
            return;
        } else {
            OpenHashSet<Disposable> set;
            disposed = true;
            set = resources;
            resources = null;
            dispose(set);
        }
    }

    @Override
    public synchronized boolean isDisposed() {
        return disposed;
    }

    @Override
    public synchronized boolean add(Disposable d) {
        ObjectHelper.requireNonNull(d, "d is null");
        OpenHashSet<Disposable> set = resources;
        if (set == null) {
            set = new OpenHashSet<>();
            resources = set;
        }
        if (set.add(d) && !d.isDisposed()) {
            disposed = false;
        }
        return true;
    }

    /**
     * Atomically adds the given array of Disposables to the container or
     * disposes them all if the container has been disposed.
     *
     * @param ds the array of Disposables
     * @return true if the operation was successful, false if the container has been disposed
     */
    public synchronized boolean addAll(Disposable... ds) {
        ObjectHelper.requireNonNull(ds, "ds is null");
        OpenHashSet<Disposable> set = resources;
        if (set == null) {
            set = new OpenHashSet<>(ds.length + 1);
            resources = set;
        }
        for (Disposable d : ds) {
            ObjectHelper.requireNonNull(d, "d is null");
            if (set.add(d) && !d.isDisposed()) {
                disposed = false;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean remove(Disposable d) {
        if (delete(d)) {
            d.dispose();
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean delete(Disposable d) {
        ObjectHelper.requireNonNull(d, "Disposable item is null");
        OpenHashSet<Disposable> set = resources;
        if (disposed || set == null || !set.remove(d)) {
            return false;
        }
        if (set.size() == 0) disposed = true;
        return true;
    }

    /**
     * Atomically clears the container, then disposes all the previously contained Disposables.
     */
    public synchronized void clear() {
        if (disposed) {
            return;
        } else {
            OpenHashSet<Disposable> set = resources;
            disposed = true;
            resources = null;

            dispose(set);
        }
    }

    /**
     * Returns the number of currently held Disposables.
     *
     * @return the number of currently held Disposables
     */
    public synchronized int size() {
        if (disposed) {
            return 0;
        } else {
            OpenHashSet<Disposable> set = resources;
            return set != null ? set.size() : 0;
        }
    }

    /**
     * Dispose the contents of the OpenHashSet by suppressing non-fatal
     * Throwables till the end.
     *
     * @param set the OpenHashSet to dispose elements of
     */
    public synchronized void dispose(OpenHashSet<Disposable> set) {
        if (set == null) {
            return;
        }
        List<Throwable> errors = null;
        Object[] array = set.keys();
        for (Object o : array) {
            if (o instanceof Disposable) {
                try {
                    ((Disposable) o).dispose();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(ex);
                }
            }
        }
        if (errors != null) {
            if (errors.size() == 1) {
                throw ExceptionHelper.wrapOrThrow(errors.get(0));
            }
            throw new CompositeException(errors);
        }
    }
}
