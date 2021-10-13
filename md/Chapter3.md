# 3.1 코틀린에서 컬렉션 만들기

### 3장에서 다루는 내용

- 컬렉션, 문자열, 정규식을 다루기 위한 함수
- 이름 붙인 인자, 디폴트 파라미터 값, 중위 호출 문법 사용
- 확장 함수와 확장 프로퍼티를 사용해 자바 라이브러리 적용
- 최상위 및 로컬 함수와 프로퍼티를 사용해 코드 구조화

---

좀 더 단순하게 숫자로 이뤄진 집합을 만들어보자.

```kotlin
val set = hashSetOf(1, 7, 53)
```

비슷한 방법으로 리스트와 맵도 만들 수 있다.

```kotlin
val list = arrayListOf(1, 7, 53)
val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
```

여기서 to가 언어가 제공하는 특별한 키워드가 아니라 일반 함수라는 점에 유의해야 한다.  자세한 내용은 나중에 다룬다.

다음 예제를 실행하면 위에서 만든 객체가 어떤 클래스에 속하는지 알 수 있다.

```kotlin
println(set.javaClass)
// class java.util.HashSet

println(list.javaClass)
// class java.util.ArrayList

println(map.javaClass)
// class java.util.HashMap
```

이로서 코틀린이 자신만의 컬렉션 기능을 제공하지 않는다는 뜻이된다. 코틀린이 자체 컬렉션을 제공하지 않는 이유는 뭘까? 표준 자바 컬렉션을 활용하면 자바 코드와 상호작용하기가 훨씬 더 쉽다. **자바에서 코틀린 함수를 호출하거나 코틀린에서 자바 함수를 호출할 때 자바와 코틀린 컬렉션을 서로 변환할 필요가 없다.**

코틀린 컬렉션은 자바 컬렉션과 똑같은 클래스지만 코틀린에서는 자바보다 더 많은 기능을 쓸 수 있다. 예를 들어 리스트의 마지막 원소를 가져오거나 수로 이뤄진 컬렉션에서 최댓값을 찾을 수 있다.

# 3.2 함수를 호출하기 쉽게 만들기

자바 컬렉션에는 디폴트 toString 구현이 들어있지만 그 디폴트 toString의 출력 형식은 고정돼 있고 우리에게 필요한 형식이 아닐 수도 있다.

```kotlin
val list = listOf(1,2,3)
println(list)  //. 여기서 toString() 호출
// [1,2,3]
```

디폴트 구현과 달리 (1;2;3) 처럼 원소 사이를 세미콜론으로 구분하고 괄호로 리스트를 둘러싸고 싶다면 어떻게 해야 할까? 코틀린에는 이런 요구 사항을 처리할 수 있는 함수가 표준 라이브러리에 이미 들어있다.

처음엔 함수 선언을 간단하게 만들 수 있게 코틀린이 지원하는 여러 기능을 사용하지 않고 함수를 직접 구현한 후 좀 더 코틀린답게 같은 함수를 다시 구현한다.

다음 리스트의 joinToString 함수는 컬렉션의 원소를 StringBuilder의 뒤에 덧붙인다. 이때 원소 사이에 구분자를 추가하고, StringBuilder의 맨 앞과 맨 뒤에는 접두사와 접미사를 추가한다.

```kotlin
fun <T> joinToString (
    collection: Collection<T>,
    separator: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}
```

이 함수는 제네릭하다. 즉, 이 함수는 어떤 타입의 값을 원소로 하는 컬렉션이든 처리할 수 있다. 제네릭 함수의 문법은 자바와 비슷하다.

```kotlin
val list = listOf(1, 2, 3)
println(joinToString(list, "; ", "(", ")"))
// (1; 2; 3)
```

잘 작동하지만 선언 부분을 좀 더 고민해봐야 한다. 어떻게 하면 이 함수를 호출하는 문장을 덜 번잡하게 만들 수 있을지 그 방법을 알아보자.

## 3.2.1 이름 붙인 인자

해결하고픈 첫 번째 문제는 함수 호출 부분의 가독성이다. 예를 들어 다음과 같은 joinToString 호출을 살펴보자.

```kotlin
joinToString(collection, " ", " ", ".")
```

인자로 전달한 각 문자열이 어떤 역할을 하는지 구분할 수 있는가? 각 원소는 공백으로 구분 될까, 마침표로 구분될까? 함수의 시그니처를 살펴보지 않고는 어렵다.

이런 문제는 특히 불리언 플래그 값을 전달해야 하는 경우 흔히 발생한다. 이를 해결하기 위해 일부 자바 코딩 스타일에서는 불리언 대신 enum 타입을 사용하라고 권장하고 파랄미터 이름을 주석에 넣으라고 요구하기도 한다.

코틀린에서는 다음과 같이 할 수 있다.

```kotlin
joinToString(collection, separator = " ", prefix = " ", postfix = ".")
```

코틀린으로 작성한 함수를 호출할 때는 함수에 전달하는 인자 중 일부(또는 전부)의 이름을 명시할 수 있다. 호출 시 인자 중 하나라도 이름을 명시하고 나면 혼동을 막기 위해 그 뒤에 오는 모든 인자에 이름을 명시해야 한다.

## 3.2.2 디폴트 파라미터 값

자바에서는 일부 클래스에서 오버로딩한 메소드가 너무 많아진다는 문제가 있다. 파라미터 이름과 타입이 계속 반복되며, 설명을 반복해서 달아야할 수도 있다. 그리고 인자 중 일부가 생략된 오버로드 함수를 호출할 때 어떤 함수가 불릴지 모호한 경우가 생긴다.

코틀린에서는 함수 선언에서 파라미터의 디폴트 값을 지정할 수 있으므로 이런 오버로드 중 상당수를 피할 수 있다. 디폴트 값을 사용해 joinToString 함수를 개선해보자. 대부분의 경우 아무 접두사나 접미사 없이 바로 콤마로 원소를 구분한다. 따라서 그런 값을 디폴트로 지정하자.

```kotlin
fun <T> joinToString (
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}
```

이제 함수를 호출할 때 모든 인자를 쓸 수도 있고, 일부를 생략할 수도 있다.

```kotlin
joinToString(list, ", ", "", "")
// 1, 2, 3
joinToString(list)
// 1, 2, 3
joinToString(list, "; ")
// 1; 2; 3
```

이름 붙인 인자를 사용하는 경우에는 인자 목록의 중간에 있는 인자를 생략하고, 지정하고 싶은 인자를 이름을 붙여서 순서와 관계없이 지정할 수 있다.

```kotlin
joinToString(list, postfix = ";", prefix = "# ")
// # 1, 2, 3;
```

## 3.2.3 정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티

자바에서는 모든 코드를 클래스의 메소드로 작성해야 한다는 사실을 알고 있고 이런 구조는 잘 작동하지만 실전에서는 어느 한 클래스에 포함시키기 어려운 코드가 많이 생긴다. 일부 연산에는 비슷하게 중요한 역할을 하는 클래스가 둘 이상 있을 수도 있다. 중요한 객체는 하나뿐이지만 그 연산을 객체의 인스턴스 API에 추가해서 API를 너무 크게 만들고 싶지는 않은 경우도 있다.

그 결과 다양한 정적 메소드를 모아두는 역할만 담당하며, 특별한 상태나 인스턴스 메소드는 없는 클래스가 생겨난다. JDK의 Collections 클래스가 전형적인 예다. 우리가 작성한 코드에서 비슷한 예를 보고 싶다면 Util이 이름에 들어있는 클래스를 찾으면 된다.

코틀린에서는 이런 무의미한 클래스가 필요 없다. 대신 함수를 직접 소스 파일의 최상위 수준, 모든 다른 클래스의 밖에 위치시키면 된다. 그런 함수들은 여전히 그 파일의 맨 앞에 정의된 패키지의 멤버 함수이므로 다른 패키지에서 그 함수를 사용하고 싶을 때는 그 함수가 정의된 패키지를 임포트해야 한다. 하지만 임포트 시 유틸리티 클래스 이름이 추가로 들어갈 필요는 없다.

```kotlin
package strings

fun joinToString(...): String {...}
```

코틀린만 사용하는 경우에는 그냥 이런 클래스가 생긴다는 사실만 기억하면 된다. 하지만 이 함수를 자바 등의 다른 JVM 언어에서 호출하고 싶다면 코드가 어떻게 컴파일되는지 알아야 최상위 함수를 사용할 수 있다. 어떻게 코틀린이 위의 join.kt를 컴파일하는지 보여주기 위해 컴파일한 결과와 같은 클래스를 자바 코드로 써보면 다음과 같다.

```java
package strings;

public class JoinKt {
	public static String joinToString(...) {...}
}
```

코틀린 컴파일러가 생성하는 클래스의 이름은 최상위 함수가 들어있던 코틀린 소스 파일의 이름과 대응한다. 코틀린 파일의 모든 최상위 함수는 이 클래스의 정적인 메소드가 된다. 따라서 자바에서 joinToString을 호출하기는 쉽다.

### 최상위 프로퍼티

함수와 마찬가지로 프로퍼티도 파일의 최상위 수준에 놓을 수 있다. 어떤 데이터를 클래스 밖에 위치시켜야 하는 경우는 흔하지는 않지만, 그래도 가끔 유용할 때가 있다.

예를 들어 어떤 연산을 수행한 횟수를 저장하는 var 프로퍼티를 만들 수 있다.

```kotlin
var opCount = 0

fun performOperation() {
	opCount++
	//...
}

fun reportOperationCount() {
	println("Operation performed $opCount times")
}
```

이런 프로퍼티의 값은 정적 필드에 저장된다. 최상위 프로퍼티를 활용해 코드에 상수를 추가할 수 있다.

```kotlin
val UNIX_LINE_SEPARATOR = "\\n" 
```

기본적으로 최상위 프로퍼티도 다른 모든 프로퍼티처럼 접근자 메소드를 통해 자바 코드에 노출된다. 겉으론 상수처럼 보이지만 실제로는 게터를 사용해야 한다면 자연스럽지 못하다. 더 자연스럽게 사용하려면 이 상수를 `public static final` 필드로 컴파일해야 한다. `const` 변경자를 추가하면 프로퍼티를 `public static final`필드로 만들 수 있다. (단, 원시 타입과 String 타입의 프로퍼티만 const로 지정할 수 있다.)

```kotlin
const val UNIX_LINE_SEPARATOR = "\\n"
```

위의 코드는 다음 자바 코드와 동등한 바이트코드를 만들어낸다.

```java
public static final String UNIX_LINE_SEPARATOR = "\\n";
```

## 3.3 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티

기존 코드와 코틀린 코드를 자연스럽게 통합하는 것은 코틀린의 핵심 목표 중 하나다. 코틀린을 기존 자바 프로젝트에 통합하는 경우에는 코틀린으로 직접 변환할 수 없거나 미처 변환하지 않은 기존 자바 코드를 처리할 수 있어야 한다. 이런 기존 자바 API를 재작성하지 않고도 코틀린이 제공하는 여러 편리한 기능을 사용할 수 있다면 정말 좋지 않을까? 바로 **확장 함수(extension function)**가 그런 역할을 해줄 수 있다.

확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수다. 확장 함수를 보여주기 위해 어떤 문자열의 마지막 문자를 돌려주는 메소드를 추가해보자.

```kotlin
package strings

fun String.lastChar(): Char = this.get(this.length - 1)
```

확장 함수를 만들려면 추가하려는 함수 이름 앞에 그 함수가 확장한 클래스의 이름을 덧붙이기만 하면 된다. 클래스 이름을 **수신 객체 타입(receiver type)**이라 부르며, 확장 함수가 호출되는 대상이 되는 값(객체)을 **수신 객체(receiver object)**라고 부른다. 위 코드에서 수신 객체  타입은 String, 수신 객체는 this 이다. (수신 객체 타입은 확장이 정의될 클래스의 타입이며, 수신 객체는 그 클래스에 속한 인스턴스 객체다.)

이 함수를 호출하는 구문은 다른 일반 클래스 멤버를 호출하는 구문과 똑같다.

```kotlin
println("Kotlin".lastChar())
// n
```

이 예제에서는 String이 수신 객체 타입이고 "Kotlin"이 수신 객체이다.

어떤 면에서 이는 String 클래스에 새로운 메소드를 추가하는 것과 같다. String 클래스가 우리가 직접 작성한 코드가 아니고, 심지어 String 클래스의 소스코드를 소유한 것도 아니지만, 우리는 여전히 원하는 메소드를 String 클래스에 추가할 수 있다. 또한 String이 자바나 코틀린 등의 언어 중 어떤 것으로 작성됐는가는 중요하지 않다.

일반 메소드의 본문에서 this를 사용할 때와 마찬가지로 확장 함수 본문에도 this를 쓸 수 있다. 그리고 일반 메소드와 마찬가지로 확장 함수 본문에서도 this를 생략할 수 있다.

확장 함수 내부에서는 일반적인 인스턴스 메소드의 내부에서와 마찬가지로 수신 객체의 메소드나 프로퍼티를 바로 사용할 수 있다. 하지만 확장 함수가 캡슐화를 깨지는 않는다는 사실을 기억하라. 클래스 안에서 정의한 메소드와 달리 **확장 함수 안에서는 클래스 내부에서만 사용할 수 있는 비공개(private) 멤버나 보호된(protected) 멤버를 사용할 수 없다.**

이제부터는 클래스의 멤버 메소드와 확장 함수를 모두 메소드라고 부를 것이다.

### 3.3.1 임포트와 확장 함수

확장 함수를 정의했다고 해도 자동으로 프로젝트 안의 모든 소스코드에서 그 함수를 사용할 수 있지는 않다. 확장 함수를 사용하기 위해서는 그 함수를 다른 클래스나 함수와 마찬가지로 임포트해야만 한다. 확장 함수를 정의하자마자 어디서든 그 함수를 쓸 수 있다면 한 클래스에 같은 이름의 확장 함수가 둘 이상 있어서 이름이 충돌하는 경우가 자주 생길 수 있다. 코틀린에서는 클래스를 임포트할 때와 동일한 구문을 사용해 개별 함수를 임포트할 수 있다.

```kotlin
import strings.lastChar

val c = "Kotlin".lastChar()
```

한 파일 안에서 다른 여러 패키지에 속해있는 이름이 같은 함수를 가져와 사용해야 하는 경우 이름을 바꿔서 임포트하면 이름 충돌을 막을 수 있다. (as keyword)

### 3.3.2 자바에서 확장 함수 호출

내부적으로 확장 함수는 수신 객체를 첫 번째 인자로 받는 정적 메소드이다. 그래서 확장 함수를 호출해도 다른 어댑터 객체나 실행 시점 부가 비용이 들지 않는다.

이런 설계로 인해 자바에서 확장 함수를 사용하기도 편한다. 단지 정적 메소드를 호출하면서 첫 번째 인자로 수신 객체를 넘기기만 하면 된다. 다른 최상위 함수와 마찬가지로 확장 함수가 들어있는 자바 클래스 이름도 확장 함수가 들어있는 파일 이름에 따라 결정된다. 따라서 확장 함수를 StringUtil.kt 파일에 정의했다면 다음과 같이 호출할 수 있다.

```java
// java
char c = StringUtil.lastChar("Java");
```

### 3.3.3 확장 함수로 유틸리티 함수 정의

이제 joinToString 함수의 최종 버전을 만들자. 이제 이 함수는 코틀린 라이브러리가 제공하는 함수와 거의 같아졌다.

```kotlin
fun <T> Collection<T>.joinToString (
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

val list = listOf(1, 2, 3)
println(list.joinToString(separator = "; ", prefix = "(", postfix = ")"))
//(1; 2; 3)
```

원소로 이뤄진 컬렉션에 대한 확장을 만들고 모든 인자에 대한 디폴트 값을 지정한다. 이제 joinToString을 마치 클래스의 멤버인 것처럼 호출할 수 있다.

```kotlin
val list = arrayListOf(1, 2, 3)
println(list.joinToString(" "))
// 1 2 3
```

확장 함수는 단지 정적 메소드 호출에 대한 문법적인 편의일 뿐이다. 그래서 **클래스가 아닌 더 구체적인 타입을 수신 객체 타입으로 지정할 수도 있다.** 그래서 문자열의 컬렉션에 대해서만 호출할 수 있는 join 함수를 정의하고 싶다면 다음과 같이 하면된다.

```kotlin
fun Collection<String>.join(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
) = joinToString(separator, prefix, postfix)

println(listOf("one", "two", "eight").join(" "))
// one two eight
```

이 함수를 객체의 리스트에 대해 호출할 수는 없다.

```kotlin
listOf(1,2,8).join()
// Error: Type mismatch: inferred type is List<Int> but Collection<String> was expected.
```

확장 함수가 정적 메소드와 같은 특징을 가지므로, 확장 함수를 하위 클래스에서 오버라이드할 수는 없다.

### 3.3.4 확장 함수는 오버라이드할 수 없다

코틀린의 메소드 오버라이드도 일반적인 객체지향의 메소드 오버라이드와 마찬가지이다. 하지만 확장 함수는 오버라이드할 수 없다. View와 그 하위 클래스인 Button이 있는데, Button이 상위 클래스의 click 함수를 오버라이드하는 경우를 생각해보자.

```kotlin
open class View {
	open fun click() = println("View clicked")
}

class Button: View() {
	override fun click() = println("Button clicked")
}
```

Button이 View의 하위 타입이기 때문에 View 타입 변수를 선언해도 Button 타입 변수를 그 변수에 대입할 수 있다. View 타입 변수에 대해 click과 같은 일반 메소드를 호출했는데, click을 Button 클래스가 오버라이드했다면 실제로는 Button이 오버라이드한 click이 호출된다.

하지만 다음 그림을 보면 알 수 있는 것처럼 확장은 이런 식으로 작동하지 않는다.

![Screen Shot 2021-10-13 at 9 07 30 AM](https://user-images.githubusercontent.com/52916061/137045284-b5bba747-04f9-4636-af42-575e80d8921a.png)

확장 함수는 클래스의 일부가 아니다. 확장 함수는 클래스 밖에 선언된다. 이름과 파라미터가 완전히 같은 확장 함수를 기반 클래스와 하위 클래스에 대해 정의해도 실제로는 확장 함수를 호출할 때 수신 객체로 지정한 변수의 정적 타입에 의해 어던 확장 함수가 호출될지 결정되지, 그 변수에 저장된 객체의 동적인 타입에 의해 확장 함수가 결정되지 않는다.

다음 예제는 View와 Button 클래스에 대해 선언된 두 showOff 확장 함수를 보여준다.

```kotlin
fun View.showOff() = println("I'm a view ! ")
fun Button.showOff() = println("I'm a button ! ")

val view: View = Button()
view.showOff()
// I'm a view !
// 확장 함수는 정적으로 결정된다.
```

view가 가리키는 객체의 실제 타입이 Button이지만, 이 경우 view의 타입이 View이기 때문에 무조건 View의 확장 함수가 호출된다.

확장 함수를 첫 번째 인자가 수신 객체인 정적 자바 메소드로 컴파일한다는 사실을 기억한다면 이런 동작을 쉽게 이해할 수 있다. 자바도 호출할 정적 함수를 같은 방식으로 정적으로 결정한다.

```java
//java
View view = new Buttonm();
ExtensionKt.showOff(view);
// I'm a view !
```

위 예제와 같이 확장 함수를 오버라이드할 수는 없다. 코틀린은 호출될 확장 함수를 정적으로 결정하기 때문이다.