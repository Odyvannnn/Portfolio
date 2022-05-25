import solution.StringConst
import solution.splitMessageExtension

fun main() {
	val outPutShort = StringConst.SHORT_MESSAGE.value.splitMessageExtension(maxLengthMessage = 14)
	println("result: $outPutShort")
//        result: [Lorem]
	val outPutMiddle = StringConst.MIDDLE_MESSAGE.value.splitMessageExtension(maxLengthMessage = 14)
	println("result: $outPutMiddle")
//        result: [Lorem 1/3, ipsum 2/3, dolor 3/3]
	val outPutLong = StringConst.LONG_MESSAGE.value.splitMessageExtension(maxLengthMessage = 50)
	println("result: $outPutLong")
//        result: [Lorem ipsum dolor sit amet, consectetur 1/10, adipiscing elit, sed do eiusmod tempor 2/10, incididunt ut labore et dolore 3/10, magna aliqua. Ut enim ad 4/10, minim veniam, quis 5/10, nostrud exercitation 6/10, ullamco laborious 7/10, nisi ut aliquip 8/10, ex ea commodo 9/10, consequat. 10/10]

}
