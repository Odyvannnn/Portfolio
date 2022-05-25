# Тестовое задание VK

### Решение выполнено Легиным Денисом

Язык реализации: kotlin
* Пример запуска и результат выполнения на тестовых данных: src/TestSplitter.kt
* Решение: src/solution/SplitMessageExtension.kt
* Используемые константы: src/solution/StringConst
___

### Описание задачи:

Задание:
Необходимо разбить входную строку на смс-ки То есть написать метод, который принимает строку и максимальную длину смс, а
возвращает массив строк сформированный по следующим правилам:

1) разбивать текст можно только по пробелам
2) если получилось >1 смс-ки то нужно добавить ко всем суффикс вида " 12/123" где 12 номер текущей смс 123 общее число
   смс
3) длина смс включая суффикс должна быть не больше переданного лимита
4) так как отправка смс платная важно получить минимальное число смс дополнительно гарантируется что переданную строку
   можно по всем правилам разбить на смс-ки (например нет слов длиннее лимита и прочее)

___

### Техническая реализация:

Основной метод решения задачи реализован в качестве функции расширения, для большего удобства при дальнейшем
использовании

```kotlin
fun String.splitMessageExtension(maxLengthMessage: Int): List<String> {
	if (this.isEmpty()) throw NullPointerException(StringConst.NULL_POINTER_HANDLER.value)

	numberOfMessages = ceil((this.length.toDouble() / maxLengthMessage.toDouble())).toInt()
	if (numberOfMessages == IntConst.LONELY_MESSAGE.index) {
		return listOf(this)
	}
	val splitText: List<String> = this.split(StringConst.EMPTY_SEPARATOR.value)
	return updateNumberOfMessages(splitText, maxLengthMessage)
}
```
Основной метод обработки исходных данных выполнен в качестве рекурсивной функции с перебором значений удовлетворяющих условию задачи
```kotlin
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
```

Вспомогательные константы

```kotlin
enum class StringConst(val value: String) {
   SHORT_MESSAGE("Lorem"),
   MIDDLE_MESSAGE("Lorem ipsum dolor"),
   LONG_MESSAGE("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laborious nisi ut aliquip ex ea commodo consequat."),
   NULL_POINTER_HANDLER("Current meaning is null"),
   EMPTY_SEPARATOR(" ")
}
```

```kotlin
enum class IntConst(val index: Int){
	SPECIAL_SYMBOLS(2),
	LONELY_MESSAGE(1)
}
```
Пример использования, результат работы на тестовых данных
```kotlin
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

```
