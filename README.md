# JuEncryptionManager

Android용 문자열 암복호화 라이브러리입니다. 현재 설정은 minSdk 26, compileSdk 35, Java/JVM target 11, Kotlin 2.0.21, AGP 8.9.1, Gradle 8.11.1입니다. AGP 실행에는 JDK 17 이상이 필요합니다.

## 사용 방법

이 저장소의 Android 프로젝트에서는 `implementation(project(":JuEncryptionManager"))`로 모듈을 참조합니다. 외부 배포 좌표의 사용 가능 여부는 검증하지 않았습니다.

```kotlin
import com.ju531.juencryptionmanager.EncryptionManager
import com.ju531.juencryptionmanager.model.*
import java.util.Base64
import javax.crypto.KeyGenerator

val manager = EncryptionManager()
val key = Base64.getEncoder().encodeToString(
    KeyGenerator.getInstance("AES").apply { init(256) }.generateKey().encoded
)
val type = EncryptionType.AES_GCM_NO_PADDING
when (val result = manager.encrypt(Encrypt(type, "예제 문자열", key))) {
    is EncryptionResult.Success -> {
        val saved = result.data // data와 iv를 함께 보관
        val restored = manager.decrypt(Decrypt(type, saved.data, key, saved.iv))
        // restored도 Success/Error를 분기하여 처리. 민감한 값은 로그에 출력하지 않음.
    }
    is EncryptionResult.Error -> { /* 입력/키/프로바이더 오류 처리 */ }
}
```

`encrypt(Encrypt): EncryptionResult<EncryptResultData>`는 UTF-8 문자열을 암호화하여 Base64 암호문과 IV를 반환합니다. `decrypt(Decrypt): EncryptionResult<DecryptResultData>`는 같은 알고리즘·키·IV를 받아 UTF-8 문자열을 복원합니다. 실패는 `EncryptionResult.Error`로 반환합니다. 위 예제는 설명용이며 Android 기기에서 실행 검증하지 않았습니다.

## 호환성과 주의사항

- AES 키는 비밀번호가 아니라 16/24/32바이트 원시 키의 Base64입니다. RSA 암호화에는 X.509 공개키, 복호화에는 PKCS#8 개인키의 Base64가 필요합니다. PEM 헤더는 입력에 포함하지 않습니다.
- 키는 안전하게 별도 보관해야 합니다. 예제처럼 매번 생성하면 이전 암호문을 복원할 수 없습니다. 키, 평문, 명령/결과 객체 전체를 로그에 남기지 마세요.
- GCM은 128비트 태그를 사용하며 반환 암호문은 암호문+태그의 Base64입니다. IV를 반드시 함께 보관하고 재사용하지 마세요. 변조/잘못된 키는 복호화 실패로 처리합니다.
- 기존 enum 이름(오탈자 포함), Base64 포맷과 CBC/CTR IV 형식은 유지합니다. GCM 복호화는 GCMParameterSpec으로 처리하며 OAEP 파라미터를 IV로 잘못 해석하지 않습니다.
- RSA OAEP의 MGF1 기본값은 프로바이더마다 다를 수 있습니다. 기존 기본값은 변경하지 않았으며 다른 플랫폼과의 상호운용은 별도 검증이 필요합니다. RSA 입력 길이 제한은 키 길이/패딩에 따릅니다.
- ECB 및 RSA NoPadding은 신규 보안 설계에 권장하지 않습니다. CBC/CTR에는 인증 기능이 없고 AES NoPadding 블록 모드는 블록 크기 배수 입력이 필요합니다. 새 데이터에는 GCM 사용을 권장합니다.
- PKCS7Padding 등 지원 여부는 프로바이더에 따라 다릅니다. 지원하지 않는 변환도 Error로 반환합니다.

## 검증

`./gradlew --offline :JuEncryptionManager:testDebugUnitTest`

GCM 한글/이모지 왕복·변조·IV 누락, RSA OAEP 왕복, CTR 왕복·잘못된 입력 회귀 테스트를 포함합니다. 현재 환경에서는 기존 kapt 1.7.10 플러그인 캐시 부재로 Gradle 설정 단계에서 중단되어 테스트와 Android 빌드는 미실행입니다. 의존성을 다운로드하거나 배포하지 않았습니다.
