package pro.schmid.livedatacombination

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Helper class allowing to combine two LiveData into one
 */
class CombinedLiveData {
    companion object {
        fun <Left, Right, Result> combine(
                leftLiveData: LiveData<Left>,
                rightLiveData: LiveData<Right>,
                merge: (leftValue: Left, rightValue: Right) -> Result
        ): LiveData<Result> {

            val mediator = MediatorLiveData<Result>()

            // Internal method used to react to change to both LiveData and merge the values
            fun combineLatestData() {
                val leftValue = leftLiveData.value ?: return
                val rightValue = rightLiveData.value ?: return
                val result = merge(leftValue, rightValue)
                mediator.value = result
            }

            mediator.addSource(leftLiveData) { combineLatestData() }
            mediator.addSource(rightLiveData) { combineLatestData() }

            return mediator
        }
    }
}
