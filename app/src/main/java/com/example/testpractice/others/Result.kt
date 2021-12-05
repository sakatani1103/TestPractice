package com.example.testpractice.others

data class Result<out T>(var status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Result<T> {
            return Result(Status.ERROR, data, msg)
        }
    }
}

enum class Status{
    SUCCESS,
    ERROR
}