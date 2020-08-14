package uk.co.verifymyage.sdk

import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class VMAApi(apiId: String, apiKey: String, apiSecret: String) {
    private var url :String = "https://api-dot-verifymyage.appspot.com"
    private var apiId : String = apiId
    private var apiKey : String = apiKey
    private var apiSecret : String = apiSecret

    private val HEX_ARRAY = "0123456789ABCDEF".toCharArray()
    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF // Here is the conversion
            hexChars[j * 2] = HEX_ARRAY[v.ushr(4)]
            hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
        }

        return String(hexChars)
    }

    private fun hmac(input: String, key: String): String{
        val sha256Hmac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
        sha256Hmac.init(secretKey)

        return bytesToHex(sha256Hmac.doFinal(input.toByteArray())).toLowerCase()
    }

    private fun hmacHeader(input: String): String{
        val time = System.currentTimeMillis() / 1000
        var hmac = hmac("$apiKey$time$input", apiSecret)
        return "$apiId:$time:$hmac"
    }

    fun request(method: String, endpoint: String, customer: VmaCustomer): JSONObject {
        val response = StringBuilder()
        val url = URL("$url$endpoint")
        val postData: ByteArray = customer.toJsonString().toByteArray()

        with(url.openConnection() as HttpURLConnection){
            requestMethod = method
            connectTimeout = 300000
            connectTimeout = 300000
            doOutput = true

            setRequestProperty("Accept", "application/json")
            setRequestProperty("Content-Type", "application/json")
            println("=============================")
            println(customer.id)
            println("=============================")
            setRequestProperty("Authorization", hmacHeader(customer.id))
            try {
                val outputStream = DataOutputStream(outputStream)
                outputStream.write(postData)
                outputStream.flush()
            } catch (exception: Exception) {
                println("Exception while posting data ${exception.message}")
            }
            println("Response code: $responseCode")

            var inputStream = errorStream
            if (errorStream == null) {
                inputStream = getInputStream()
            }

            try{
                BufferedReader(
                    InputStreamReader(inputStream, "utf-8")
                ).use { br ->
                    var responseLine: String? = null
                    while (br.readLine().also { responseLine = it } != null) {
                        response.append(responseLine!!.trim { it <= ' ' })
                    }

                }
                println("============= RESPONSE FROM [POST] $endpoint ===================")
                println(response.toString())
                println("===============================================================\n")
            } catch (exception: Exception) {
                println("Exception while reading data ${exception.message}")

            }
        }

        return JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1))
    }
}