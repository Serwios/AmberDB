# AmberDB

## Building the Project

```bash
mvn clean package -DskipTests
```

Output jar:

```
target/amberdb.jar
```

## Running CLI

### Syntax Check Mode

```bash
java -jar amberdb.jar --query "SELECT id FROM users WHERE age > 18;"
```

### Debug AST Mode

```bash
java -jar amberdb.jar --debug-ast --query "SELECT name FROM users WHERE age >= 18 LIMIT 10;"
```

## Interactive REPL

```bash
java -jar amberdb.jar
```


