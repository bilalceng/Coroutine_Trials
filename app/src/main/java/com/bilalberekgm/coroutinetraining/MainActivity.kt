package com.bilalberekgm.coroutinetraining

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis


private const val TAG = "CoroutineController"
class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //<-------------------GlobalScope()---------------------------------->
        /**
         *        val job =  GlobalScope.launch(Dispatchers.IO){
         *             val context = coroutineContext
         *
         *             delay(2000)
         *             Log.d(TAG,
         *                 "the thread that coroutine currently work: " +
         *                         " ${Thread.currentThread().name}" +
         *                         " , coroutine coroutine context : $context "
         *
         *
         *             )
         *         }
         *         if(job.isActive){
         *             job.cancel()
         *             Log.d(
         *                 TAG,"normal execution thread: ${Thread.currentThread().name}"+
         *                         "job activation : ${job.isActive}"
         *             )
         *         }else{
         *             Log.d(
         *                 TAG,"normal execution thread: ${Thread.currentThread().name}"+
         *                 "job activation : ${job.isActive}"
         *             )
         *
         *
         *         }
         *
         */

        //<---------------------LifecycleScope--And--CoroutineScope-------->
        /**
         *         textView = findViewById(R.id.textView)
         *
         *         val job = sampleCoroutineScope()
         *         Thread.sleep(10000)
         *         Log.d(TAG, "activation situation : ${job.isActive}")
         *     }
         *
         *     private fun sampleCoroutineScope(): Job{
         *        // val scope = CoroutineScope(Job() + Dispatchers.Main)
         *         val job = lifecycleScope.launch {
         *             delay(1000)
         *             textView.text = "text is changed"
         *             Log.d(TAG, "coroutine context: $coroutineContext")
         *         }
         *         Log.d(TAG, "Lifecycle scope: $lifecycleScope")
         *         return  job
         *     }
         */
        //<-----------------------------Trials-------------------------------->
        /**
         *         textView = findViewById(R.id.textView)
         *             var comment1= ""
         *         val job = lifecycleScope.launch {
         *             launch {
         *                  comment1 = fetchCommentsFromRemote()
         *                 Log.d(TAG, "comment1 : $comment1")
         *             }.join()
         *
         *             launch {
         *                 val answer1 = fetchAnswersFromRemote()
         *                 Log.d(TAG, "answer1+comment1 : $answer1$comment1")
         *             }
         *
         *         }
         *
         *         Log.d(TAG,"activation state: ${job.isActive}")
         *     }
         *
         *     private suspend fun fetchCommentsFromRemote(): String {
         *         return withContext(Dispatchers.IO){
         *         delay(3000)
         *         "comment1"
         *     }
         *     }
         *
         *     private suspend fun fetchAnswersFromRemote(): String {
         *         return withContext(Dispatchers.IO){
         *             delay(3000)
         *             "answer1"
         *         }
         *     }
         */
        //<-------------------------Cancellation(Begining)------------------->
        /**
         *     val job = GlobalScope.launch(Dispatchers.Default) {
         *                 withTimeout(2000L){
         *                     for(i in 30..50) {
         *                         if (isActive) {
         *                             Log.d(TAG, "$i.th fibonnacci number is: ${fibonacci(i)} ")
         *
         *                         }
         *                     }
         *                 }
         *         }
         *         runBlocking {
         *             delay(3000)
         *             Log.d(TAG,"isActive: ${job.isActive}")
         *             Log.d(TAG,"isCompleted: ${job.isCompleted}")
         *             Log.d(TAG,"isCancelled: ${job.isCancelled}")
         *             Log.d(TAG,"parent: ${job.parent}")
         *             job.cancel()
         *             Log.d(TAG,"isActive: ${job.isActive}")
         *             Log.d(TAG,"isCompleted: ${job.isCompleted}")
         *             Log.d(TAG,"isCancelled: ${job.isCancelled}")
         *         }
         *     }
         *
         *     private fun fibonacci(n: Int): Int {
         *         return if (n <= 1) {
         *             n
         *         } else {
         *             fibonacci(n - 1) + fibonacci(n - 2)
         *         }
         *     }
         */
        //<-------------------------------Async()--Await()----------------------------->
        /**
         *
         *         GlobalScope.launch {
         *             val time = measureTimeMillis {
         *                 val listOfAnswers = listOf<Deferred<String>>(
         *                     async { doNetworkCall1() },
         *                     async { doNetworkCall2() }
         *                 )
         *                val newList =  listOfAnswers.awaitAll()
         *                 newList.forEach{
         *                     Log.d(TAG, it)
         *                 }
         *             }
         *             Log.d(TAG, "Tİme: $time")
         *         }
         *
         *
         *
         *     }
         *
         *     suspend private fun doNetworkCall1() = withContext(Dispatchers.IO){
         *         delay(3000L)
         *         "answer1"
         *     }
         *     suspend private fun doNetworkCall2() = withContext(Dispatchers.IO){
         *         delay(3000L)
         *         "answer2"
         *     }
         */
        textView = findViewById(R.id.textView)

        val handler = CoroutineExceptionHandler { _, throwable ->
            println("Caught exception: $throwable")
        }

        //handlers can not caught cancellation exceptions. they are handled by default.
     lifecycleScope.launch(handler) {
                    launch {
                        launch {
                            throw Exception("Error")
                        }
                    }
            }

       val result =  lifecycleScope.async {
            "result"
           throw  Exception("error")
        }

        //do not do this
        lifecycleScope.launch {
            try {
                result.await()
            }catch (e:Exception) {
                println("success${e.stackTrace}")
            }
        }
        //when wrap your child coroutines with superVisorScope{} even if the one of the
        // the children is failed the rest of the children continue to work.
        //when using CoroutineScope() scope for launching coroutines if one child coroutine is filed all the other children is failed.
        CoroutineScope(Dispatchers.Main + handler).launch{
            supervisorScope {
                launch {
                    delay(300L)
                    throw Exception("coroutine1 is failed")
                }
                launch {
                    delay(500L)
                    println("coroutine2 succeed")
                }
            }
        }

        GlobalScope.launch(handler + Dispatchers.Main){
                launch {
                    delay(300L)
                    throw Exception("coroutine1 is failed in lifecycleScope")
                }
                launch {
                    delay(500L)
                    println("coroutine2 succeed in lifecycleScope")
                }
        }

        MainScope().launch(Dispatchers.Main) {

        }
    }
}