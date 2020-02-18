package br.com.aisdigital.retrofit2.mock.testutil

import java.util.concurrent.*

class SynchronousExecutorService : ExecutorService {

    override fun shutdown() {

    }

    override fun shutdownNow(): List<Runnable>? {
        return null
    }

    override fun isShutdown(): Boolean {
        return false
    }

    override fun isTerminated(): Boolean {
        return false
    }

    @Throws(InterruptedException::class)
    override fun awaitTermination(timeout: Long, unit: TimeUnit): Boolean {
        return false
    }

    override fun <T> submit(task: Callable<T>): Future<T>? {
        return null
    }

    override fun <T> submit(task: Runnable, result: T): Future<T>? {
        return null
    }

    override fun submit(task: Runnable): Future<*>? {
        return null
    }

    @Throws(InterruptedException::class)
    override fun <T> invokeAll(tasks: Collection<Callable<T>>): List<Future<T>>?{
        return null
    }

    @Throws(InterruptedException::class)
    override fun <T> invokeAll(tasks: Collection<Callable<T>>, timeout: Long, unit: TimeUnit): List<Future<T>>? {
        return null
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    override fun <T> invokeAny(tasks: Collection<Callable<T>>): T? {
        return null
    }

    @Throws(InterruptedException::class, ExecutionException::class, TimeoutException::class)
    override fun <T> invokeAny(tasks: Collection<Callable<T>>, timeout: Long, unit: TimeUnit): T? {
        return null
    }

    override fun execute(command: Runnable) {
        command.run()
    }
}