# 1장 - 코틀린이란 무엇이며 왜 필요한가?

코틀린은 자바 플랫폼에서 돌아가는 새로운 프로그래밍 언어입니다. 간결하고 실용적이며, 자바 코드와의 상호 운용성을 중시합니다. 

<br/>

## 1.1 코틀린 맛보기

다음 코드를 살펴봅시다.

```kotlin
data class Person(
  val name: String,
  val age: Int? = null
)

fun main(args: Array<String>) {
  val persons = listOf(
    Person("영희"),
    Person("철수", age = 29)
  )
  
  val oldest = persons.maxBy { it.age ?: 0 }
  println("나이가 가장 많은 사람: $oldest")
}
```

<br/>

## 1.2 코틀린의 주요 특성

- 대상 플랫폼 : 서버, 안드로이드 등 자바가 실행되는 모든 곳. 자바 뿐 아니라 자바스크립트로도 코틀린을 컴파일할 수 있습니다.

  

- 정적 타입 지정 언어 : 모든 프로그램 구성 요소의 타입을 컴파일 시점에 알 수 있고 컴파일러가 타입을 검증해준다는 뜻입니다. 그루비나 JRuby 같은 동적 타입 지정 언어와 다릅니다. 

  자바와 달리 코틀린에서는 모든 변수의 타입을 프로그래머가 직접 명시할 필요가 없습니다. 대부분의 경우 컴파일러가 자동으로 유추할 수 있기 때문에 타입 선언을 생략해도 됩니다.

  ```kotlin
  var x = 1
  ```

  정적 타입 지정의 장점은 다음과 같습니다.

  - **성능** : 실행 시점에 어떤 메소드를 호출할지 알아내는 과정이 필요 없으므로 메소드 호출이 더 빠릅니다.
  - **신뢰성** : 컴파일러가 프로그램의 정확성을 검증하기 때문에 실행 시 오류로 중단될 가능성이 더 적어집니다.
  - **유지 보수성** : 코드에서 다루는 객체가 어떤 타입에 속하는지 알 수 있기 때문에 처음 보는 코드를 다룰 때도 더 쉽습니다.
  - **도구 지원** : 정적 타입 지정을 활용하면 더 안전하게 리팩토링 할 수 있고, 도구는 더 정확한 코드 완성 기능을 제공해줍니다.

  가장 중요한 특성은 **코틀린은 nullable 타입을 지원한다**는 점입니다. 

  

- 함수형 프로그래밍과 객체형 프로그래밍

  함수형 프로그래밍의 핵심 개념은 다음과 같습니다.

  - **일급 시민인 함수** : 함수를 일반 값처럼 다룰 수 있습니다. 함수를 변수에 저장할 수 있고, 함수를 인자로 다른 함수에 전달할 수 있으며, 함수에서 새로운 함수를 만들어서 반환할 수 있습니다.

    ```kotlin
    val func: () -> String = { "Hello" }
    
    fun invokeFunc(func: () -> String)
    
    fun returnFunc(): () -> String {
      return func
    }
    ```

  - **불변성** : 함수형 프로그래밍에서는 일단 만들어지고 나면 내부 상태가 절대로 바뀌지 않는 불편 객체를 사용해 프로그램을 작성합니다.

  - **부수 효과 없음** : 함수형 프로그래밍에서는 입력이 같으면 항상 같은 출력을 내놓고 다른 객체의 상태를 변경하지 않으며, 함수 외부나 다른 바깥 환경과 상호작용하지 않는 순수 함수를 사용합니다.

  

  ### 함수형 프로그래밍의 이점

  - **간결성** : 명령형 코드에 비해 더 간결하며 우아합니다. 함수를 값처럼 활용할 수 있으면 더 강력한 추상화를 할 수 있습니다.

    비슷한 작업을 수행하는 비슷한 두 개의 코드 조각이 있다고 가정해봅시다. 하지만 두 코드 조각은 일부 세부 사항에서 차이가 납니다. 이때 로직에서 공통부분을 따로 함수로 뽑아내고 서로 다른 세부 사항을 인자로 전달할 수 있습니다.

    ```kotlin
    fun findAlice() = findPerson { it.name == "Alice" }
    fun findBob() = findPerson { it.name = "Bob" }
    ```

  - **다중 스레드에서 안전함** : 적절한 동기화 없이 같은 데이터를 여러 스레드가 변경하는 경우 많은 문제가 생깁니다. 불변 데이터 구조를 사용하고 순수 함수를 그 데이터 구조에 적용한다면 다중 스레드 환경에서 같은 데이터를 여러 스레드가 변경할 수 없습니다. 따라서 복잡한 동기화를 적용하지 않아도 됩니다.

  - **테스트 용이성** : 부수 효과가 있는 함수는 그 함수를 실행할 때 전체 환경을 구성하는 준비 코드가 따로 필요하지만 순수 함수는 그런 준비 코드 없이 독립적으로 테스트 할 수 있습니다.

  

  물론 자바에서도 함수형 프로그래밍이 가능하지만 편하게 사용하기에 충분한 라이브러리와 문법을 지원하지 않습니다. 코틀린은 처음부터 함수형 프로그래밍을 다음처럼 지원해왔습니다.

  - 함수 타입을 지원함에 따라 어떤 함수가 다른 함수를 파라미터로 받거나 함수가 새로운 함수를 반환할 수 있습니다. 
  - 람다식을 지원함에 따라 번거로운 준비 코드를 작성하지 않아도 코드 블록을 쉽게 정의하고 여기저기 전달할 수 있습니다.
  - 데이터 클래스는 불변적인 값 객체를 간편하게 만들 수 있는 구문을 제공합니다.
  - 코틀린 표준 라이브러리는 객체와 컬렉션을 함수형 스타일로 다룰 수 있는 API를 제공합니다.

- 무료 오픈소스 : 코틀린 언어와 컴파일러, 라이브러리 및 도구는 모두 오픈소스이다.

<br/>

## 1.3 코틀린 응용

코틀린이 각 분야에 적합한 언어인 이유를 살펴봅시다.

- 코틀린 서버 프로그래밍 - 자바 코드와 매끄럽게 상호운용할 수 있는 코틀린을 사용하면 몇 가지 새로운 기술을 활용하여 서버 시스템을 개발할 수 있습니다. 예를 들면 코틀린이 제공하는 깔끔하고 간결한 DSL 기능을 활용하여 영속성 프레임워크인 `Exposed` 프레임워크(SQL 데이터 베이스의 구조를 기술할 수 있는 읽기 쉬운 DSL. 코틀린 코드만을 사용하여 완전한 타입 검사를 지원하며 질의를 실행할 수 있습니다.)가 있습니다.

  ```kotlin
  // 테이블
  object CountryTable: IdTable() { 
    val name = varchar("name", 250).uniqueIndex()
    val iso = varchar("iso", 2).uniqueIndex()
  }
  
  // 엔티티
  class Country(id: EntityID): Entity(id) {
    var name: String by CountryTable.name
    var iso: String by CountryTable.iso
  }
  
  // 코틀린 코드만으로 쿼리 날리기
  val russia = Country.find {
    CountryTable.iso.eq("ru")
  }.first()
  println(russia.name)
  ```

- 코틀린 안드로이드 프로그래밍 - 안드로이드 스튜디오에서 지원하는 1순위 언어는 코틀린입니다. 책의 예시로 나와있는 `Anko` 라이브러리는 더 이상 사용되지 않으므로 작성하지 않겠습니다.

<br/>

## 1.4 코틀린의 철학

코틀린의 실용성, 간결성, 안정성, 상호운용성이 왜 장점으로 꼽히는지 알아보겠습니다.

- **실용성** : 코틀린은 다른 언어가 채택한 이미 성공적으로 검증된 해법과 기능에 의존합니다. 또한 특정 프로그래밍 스타일이나 패러다임을 사용할 것을 강제로 요구하지 않습니다. 마지막으로 코틀린은 인텔리제이 IDE의 개발과 컴파일러의 개발이 맞물려 이뤄져왔기 때문에 도구를 강조합니다.

- **간결성** : 코틀린은 프로그래머가 작성하는 코드에서 의미 없는 부분을 줄이고 언어가 요구하는 구조를 만족시키기 위한 많은 노력들이 모여 만들어졌습니다. `getter`, `setter`, 생성자 파라미터를 필드에 대입하기 위한 로직 등 자바에 존재하는 여러 가지 번거로운 준비 코드를 묵시적으로 제공합니다.

  컬렉션에서 원소를 찾는 것과 같은 작업을 수행하는 코드는 라이브러리 함수 호출로 쉽게 대치할 수 있으며 연산자 오버로딩을 지원하지만, 언어가 제공하지 않는 연산자를 정의할 수 있게 허용하지는 않습니다.

- **안정성** : JVM을 사용하면 메모리 안정성을 보장하고, 버퍼 오버플로를 방지하며, 동적으로 할당한 메모리를 잘못 사용함으로 인해 발생할 수 있는 다양한 문제를 예방할 수 있습니다. JVM 위에서 실행되는 코틀린은 자바보다 더 적은 비용으로 타입 안정성을 사용할 수 있습니다. 타입 추론을 제공해주며, 컴파일 시점 검사를 통해 실행 시점 오류를 더 많이 방지해줍니다. 코틀린의 타입 시스템은 null이 될 수 없는 값을 추적하며, 실행 시점에 NPE(`NullPointerException`)이 발생할 수 있는 연산을 금지합니다.

  ```kotlin
  val s: String? = null // null 가능 타입
  val s2: String = "" // null 불가능 타입
  ```

  방지해주는 또 다른 예외로는 `ClassCastException`이 있습니다. 어떤 객체를 다른 타입으로 cast하기 전에 타입을 미리 검사하지 않으면 해당 익셉션이 발생할 수 있는데 코틀린에서는 타입 검사와 cast가 한 연산자에 의해 이뤄집니다.

  ```kotlin
  if (value is String) // 타입 검사
  	println(value.toUpperCase()) // 해당 타입의 메소드 사용
  ```

- **상호운용성** : 코틀린의 경우 자바에서 쓰던 기존 라이브러리를 그대로 사용할 수 있습니다. 자바 메소드 호출, 상속 인터페이스 구현, 자바 어노테이션을 적용하는 등의 일이 모두 가능합니다. 다른 일부 JVM 언어와 달리 코틀린은 자바 코드에서 코틀린 코드를 호출할 때도 아무런 노력이 필요 없습니다. 이에 따라 자바 프로젝트에 코틀린을 도입하는 경우 자바를 코틀린으로 변환하는 도구를 자바 클래스에 대해 실행하여 변환할 수도 있습니다. 

  또 다른 방향으로는 기존 자바 라이브러리를 가능하면 최대한 활용한다는 점입니다. 코틀린은 자체 컬렉션 라이브러리를 제공하지 않으며 자바 표준 라이브러리 클래스에 의존하고, 더 쉽게 활용할 수 있는 몇 가지 기능을 더 제공합니다. 이는 코틀린에서 자바 API를 호출할 때 객체를 감싸거나 변환할 필요가 없고, 자바에서 코틀린 API를 호출할 때도 마찬가지로 아무런 변환이 필요없다는 뜻입니다.

  코틀린이 제공하는 도구도 다중 언어 프로젝트를 완전히 지원하는데 따라서 다음과 같은 동작이 가능합니다.

  - 자바와 코틀린 소스 파일을 자유롭게 네비게이션 할 수 있습니다.
  - 여러 언어로 이뤄진 프로젝트를 디버깅하고 서로 다른 언어로 작성된 코드를 언어와 관계없이 한 단계씩 실행할 수 있습니다.
  - 자바 메소드를 리팩토링해도 그 메소드와 관련 있는 코틀린 코드까지 제대로 변경됩니다. 역으로 코틀린 메소드를 리팩토링해도 자바 코드까지 모두 자동으로 변경됩니다.

<br/>

## 1.5 코틀린 도구 사용

컴파일 과정이 어떻게 이뤄지며 그 과정에서 어떤 도구가 쓰이는지 알아봅시다.

- **코틀린 코드 컴파일** - 코틀린 소스코드를 저장할 때는 보통 `.kt`라는 확장자를 파일에 붙입니다. 코틀린 컴파일러는 코드를 분석하여 `.class` 파일을 만들어내고 만들어진 `.class` 파일은 개발 중인 애플리케이션의 유형에 맞는 표준 패키징 과정을 거쳐 실행될 수 있습니다. 

  ![Migrating an Android App from Java to Kotlin | by Philipp Hofmann | Monster  Culture | Medium](https://miro.medium.com/max/1400/1*mIEJKaGXgH8fgy3tEd9kRA.png)

  코틀린 컴파일러로 컴파일한 코드는 코틀린 런타임 라이브러리에 의존합니다. 런타임 라이브러리에는 코틀린 자체 표준 라이브러리 클래스와 코틀린에서 자바 API의 기능을 확장한 내용이 들어있습니다. 코틀린으로 컴파일한 애플리케이션을 배포할 때는 런타임 라이브러리도 함께 배포해야 합니다.

- **인텔리제이 IDE와 안드로이드 스튜디오의 코틀린 플러그인**

- **대화형 셸** - kotlinc 명령을 실행하거나 인텔리제이 IDE 플러그인의 메뉴(툴 > 코틀린> 코틀린 REPL)를 사용하면 됩니다.

- **이클립스 플러그인** - 코틀린 플러그인을 설치하여 사용할 수 있습니다.

- **온라인 플레이그라운드** - https://play.kotlinlang.org/ 에 접속하면 온라인으로 코드를 작성하고 컴파일 해볼 수 있습니다.

- **자바-코틀린 변환기** - 인텔리제이 메뉴에서 코드 > 자바 파일을 코틀린 파일로 변환을 선택하여 변환할 수 있습니다.

