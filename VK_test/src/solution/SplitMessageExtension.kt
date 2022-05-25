package solution

import kotlin.math.ceil

/*
Задание:
Необходимо разбить входную строку на смс-ки
То есть написать метод, который принимает строку и максимальную длину смс, а возвращает массив строк сформированный
по следующим правилам:
1) разбивать текст можно только по пробелам
2) если получилось >1 смс-ки то нужно добавить ко всем суффикс вида " 12/123" где 12 номер текущей смс 123 общее число смс
3) длина смс включая суффикс должна быть не больше переданного лимита
4) так как отправка смс платная важно получить минимальное число смс
дополнительно гарантируется что переданную строку можно по всем правилам разбить на смс-ки (например нет слов длиннее
лимита и прочее)
 */

private var numberOfMessages: Int = 2
private val globalText = mutableListOf<String>()

fun String.splitMessageExtension(maxLengthMessage: Int): List<String> {
	if (this.isEmpty()) throw NullPointerException(StringConst.NULL_POINTER_HANDLER.value)

	numberOfMessages = ceil((this.length.toDouble() / maxLengthMessage.toDouble())).toInt()
	if (numberOfMessages == IntConst.LONELY_MESSAGE.index) {
		return listOf(this)
	}
	val splitText: List<String> = this.split(StringConst.EMPTY_SEPARATOR.value)
	return updateNumberOfMessages(splitText, maxLengthMessage)
}

fun updateNumberOfMessages(splitText: List<String>, maxLengthMessage: Int): List<String> {
	val localText = mutableListOf<String>()
	var localLength = 0
	var numOfMessageCounter = 1
	for ((index, word) in splitText.withIndex()) {
		try {
			localText.add(word)
			localLength += word.length

			if ((localLength + splitText[index + 1].length + index + 1 + IntConst.SPECIAL_SYMBOLS.index + numberOfMessages.toString().length) >= maxLengthMessage) {
				globalText.add(localText.joinToString(StringConst.EMPTY_SEPARATOR.value) + " ${numOfMessageCounter}/$numberOfMessages")
				localText.clear()
				localLength = 0
				numOfMessageCounter++
			}
		} catch (e: Exception) {
			globalText.add(localText.joinToString(StringConst.EMPTY_SEPARATOR.value) + " ${numOfMessageCounter}/$numberOfMessages")
			localText.clear()
			localLength = 0
		}
	}
	if (globalText.size != numberOfMessages) {
		numberOfMessages++
		globalText.clear()
		updateNumberOfMessages(splitText, maxLengthMessage)
	}
	return globalText
}