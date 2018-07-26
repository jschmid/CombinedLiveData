package pro.schmid.livedatacombination

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CombinedLiveDataTest {

    @Rule
    @JvmField
    var viewModelRule: TestRule = InstantTaskExecutorRule()

    @Test
    fun simpleTest() {

        val data = MutableLiveData<Int>()

        assertNull(data.value)

        data.postValue(1337)

        assertEquals(1337, data.value)
    }

    @Test
    fun failingTest() {
        val data1 = MutableLiveData<Int>()
        val data2 = MutableLiveData<Int>()

        val combined = CombinedLiveData.combine(data1, data2) { left, right -> left * right }

        val observer = mock<Observer<Int>>()
        combined.observeForever(observer)

        verify(observer, never()).onChanged(any())

        data1.value = 2
        data2.value = 3

        verify(observer).onChanged(6)
    }

}