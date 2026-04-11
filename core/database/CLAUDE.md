# Core Database

Room 3 기반 로컬 데이터베이스. api(인터페이스)와 impl(구현체)로 분리된다.

## 지침

### Entity

- 클래스명: `{Name}LocalEntity`
- `@Entity(tableName = "...")` 명시적 테이블명 지정
- `@ColumnInfo(name = "...", defaultValue = "...")` 사용

### Dao

- 클래스명: `{Name}Dao`
- `RoomDao<E>` 기본 인터페이스를 확장하여 `upsert()`, `delete()` 제공
- 반환 타입: `Flow<T?>` (단건), `Flow<List<T>>` (목록), `PagingSource<Int, T>` (페이징)
- 복잡한 쿼리는 테이블 별칭 사용: `Tag AS FilterTag`

### Migration

- `@Database(autoMigrations = [AutoMigration(from = X, to = Y)])` 사용
- 복잡한 마이그레이션은 `MigrationTestHelper`로 테스트

### TypeConverter

- `UuidTypeConverter`, `LocalDateTypeConverter`, `LocalDateTimeTypeConverter` (library/room-common)
- Enum은 `persistentValue: Int`로 변환
