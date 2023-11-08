package unit.chip.lib_unit_chip.model


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


import android.util.Log
import net.sf.scuba.smartcards.CardFileInputStream
import net.sf.scuba.tlv.TLVInputStream
import org.jmrtd.lds.DataGroup
import java.io.*
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class DGr13File(cardFileInputStream: CardFileInputStream) : DataGroup(109, cardFileInputStream) {

    companion object {
        val f525r = Logger.getLogger("org.jmrtd")
        val charId = charArrayOf('0', 17.toChar(), 2.toChar(), 1.toChar(), 1.toChar(), 19.toChar(), '\u000C')
        val charName = charArrayOf('0', 28.toChar(), 2.toChar(), 1.toChar(), 2.toChar(), '\u000C', 23.toChar())
        val charBirthDay = charArrayOf('0', 15.toChar(), 2.toChar(), 1.toChar(), 3.toChar(), 19.toChar(), '\n')
        val charGender = charArrayOf('0', '\b', 2.toChar(), 1.toChar(), 4.toChar(), '\u000C', 3.toChar())
        val charNationality = charArrayOf('0', 15.toChar(), 2.toChar(), 1.toChar(), 5.toChar(), '\u000C', '\n')
        val charNation = charArrayOf('0', '\t', 2.toChar(), 1.toChar(), 6.toChar(), '\u000C', 4.toChar())
        val charReligion = charArrayOf('0', 11.toChar(), 2.toChar(), 1.toChar(), 7.toChar(), '\u000C', 6.toChar())
        val charHomeTown = charArrayOf('0', '&', 2.toChar(), 1.toChar(), '\b', '\u000C', '!')
        val charRecentLocation = charArrayOf('0', '=', 2.toChar(), 1.toChar(), '\t', '\u000C', '8')
        val charDescription = charArrayOf('0', '(', 2.toChar(), 1.toChar(), '\n', '\u000C', '#')
        val charIssueDate = charArrayOf('0', 15.toChar(), 2.toChar(), 1.toChar(), 11.toChar(), 19.toChar(), '\n')
        val charExpiredDate = charArrayOf('0', 15.toChar(), 2.toChar(), 1.toChar(), '\u000c', '\u000C', '\n')
        val f538E = charArrayOf('0', '6', 2.toChar(), 1.toChar(), '\r')
        val charFatherName = charArrayOf('0', 25.toChar(), '\u000C', 23.toChar())
        val charMotherName = charArrayOf('0', 22.toChar(), '\u000C', 20.toChar())
        val charOldNumber = charArrayOf('0', 14.toChar(), 2.toChar(), 1.toChar(), 15.toChar(), 19.toChar(), '\t')
        val charUnkIdNumber = charArrayOf('0', 21.toChar(), 2.toChar(), 1.toChar(), 16.toChar(), 19.toChar(), 16.toChar())
    }

    var id: String? = null
    var name: String? = null
    var birthDay: String? = null
    var gender: String? = null
    var nationality: String? = null
    var nation: String? = null
    var religion: String? = null
    var homeTown: String? = null
    var recentLocation: String? = null
    var description: String? = null
    var issueDate: String? = null
    var expiredDate: String? = null
    var fatherName: String? = null
    var motherName: String? = null
    var partnerName: String? = null
    var oldNumber: String? = null
    var unkIdNumber: String? = null
    var listData = ArrayList<String>()

    init {
        listData = ArrayList()
    }

    override fun readContent(inputStream: InputStream) {
        try {
            val inputStreamToUse: TLVInputStream =
                if (inputStream is TLVInputStream) inputStream else TLVInputStream(inputStream)

            val cArr = CharArray(2048)
            f525r.log(
                Level.INFO,
                "numRead",
                Integer.valueOf(
                    BufferedReader(
                        InputStreamReader(
                            DataInputStream(inputStreamToUse),
                            Charsets.UTF_8
                        )
                    ).read(cArr)
                ))
            val arrayList = ArrayList<Int>()
            var i = 1
            var i2 = 0
            while (true) {
                val i3 = i2
                if (i3 >= 2043) {
                    break
                }
                val c = cArr[i3]
                val i4 = i3 + 1
                val c2 = cArr[i4]
                val c3 = cArr[i3 + 2]
                val c4 = cArr[i3 + 3]
                val c5 = cArr[i3 + 4]
                if (c != '0' || c3 != 2.toChar() || c4 != 1.toChar() || c5 != i.toChar()) {
                    if (c == '0' && c2 == 0.toChar() && c3 == 0.toChar() && c4 == 0.toChar()) {
                        arrayList.add(i3)
                        break
                    }
                } else {
                    i++
                    arrayList.add(i3)
                }
                i2 = i4
            }
            var i5 = 0
            while (i5 < arrayList.size - 1) {
                val intValue = arrayList[i5]
                val i6 = i5 + 1
                i5 = i6
                val copyOfRange = Arrays.copyOfRange(cArr, intValue, arrayList[i6])
                if (copyOfRange.size >= 5) {
                    when (copyOfRange[4]) {
                        1.toChar() -> id =
                            String(copyOfRange.copyOfRange(charId.size, copyOfRange.size))

                        2.toChar() -> name =
                            String(copyOfRange.copyOfRange(charName.size, copyOfRange.size))

                        3.toChar() -> birthDay =
                            String(copyOfRange.copyOfRange(charBirthDay.size, copyOfRange.size))

                        4.toChar() -> gender =
                            String(copyOfRange.copyOfRange(charGender.size, copyOfRange.size))

                        5.toChar() -> nationality =
                            String(copyOfRange.copyOfRange(charNationality.size, copyOfRange.size))

                        6.toChar() -> nation =
                            String(copyOfRange.copyOfRange(charNation.size, copyOfRange.size))

                        7.toChar() -> religion =
                            String(copyOfRange.copyOfRange(charReligion.size, copyOfRange.size))

                        8.toChar() -> homeTown =
                            String(copyOfRange.copyOfRange(charHomeTown.size, copyOfRange.size))

                        9.toChar() -> recentLocation =
                            String(copyOfRange.copyOfRange(charRecentLocation.size, copyOfRange.size))

                        10.toChar() -> description =
                            String(copyOfRange.copyOfRange(charDescription.size, copyOfRange.size))

                        11.toChar() -> issueDate =
                            String(copyOfRange.copyOfRange(charIssueDate.size, copyOfRange.size))

                        12.toChar() -> expiredDate =
                            String(copyOfRange.copyOfRange(charExpiredDate.size, copyOfRange.size))

                        13.toChar() -> {
                            val arrayList = ArrayList<Int>()
                            for (length in f538E.size until copyOfRange.size - 2) {
                                if (copyOfRange[length] == '0' && copyOfRange[length + 2] == 12.toChar()) {
                                    arrayList.add(length)
                                }
                            }
                            if (arrayList.size != 2) {
                                Log.e("FAMILY", "Bad format")
                            } else {
                                fatherName = String(copyOfRange.copyOfRange(arrayList[0] + charFatherName.size, arrayList[1]))
                                motherName = String(copyOfRange.copyOfRange(arrayList[1] + charMotherName.size, copyOfRange.size))
                            }
                        }
                        14.toChar() -> { /* handle case 14 */ }
                        15.toChar() -> oldNumber = String(copyOfRange.copyOfRange(charOldNumber.size, copyOfRange.size))
                        16.toChar() -> unkIdNumber = String(copyOfRange.copyOfRange(charUnkIdNumber.size, copyOfRange.size))
                        else -> listData.add(String(copyOfRange))
                    }
                }
            }
        } catch (e: Exception) {
            f525r.log(Level.WARNING, "Exception", e)
        }
    }

    override fun writeContent(outputStream: OutputStream) {
        // Implement writeContent logic here
    }
}

