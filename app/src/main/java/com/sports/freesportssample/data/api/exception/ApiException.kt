package com.sports.freesportssample.data.api.exception

class ApiException(
    var code: Int,
    message: String?
) : Throwable(message)