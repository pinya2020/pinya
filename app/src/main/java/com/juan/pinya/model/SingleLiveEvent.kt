package com.juan.pinya.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T>(t: T? = null) : MutableLiveData<T>(t) {
    private val isChanged = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (isChanged.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    override fun setValue(value: T) {
        isChanged.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T) {
        isChanged.set(true)
        super.postValue(value)
    }
}