package com.juan.pinya.module

import com.juan.pinya.model.Stuff

interface StuffDao {
    suspend fun getStuffById(id: String): Result<Stuff>
}