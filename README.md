# Commands

simplistic, work in progress, command framework.

# Usages

| Platform    | Platform Class Name   | Help Template |
| ----------- | --------------------- | ------------- |
| bukkit      | BukkitCommandPlatform | todo          |
| kord        | todo                  | todo          |

| Command Wrapper  | Platform Class Name      |
| ---------------- | ------------------------ |
| annotation-based | AnnotationCommandWrapper |

```groovy
def versionPlatform = "a92ba104e6"
def platforms = [
        "bukkit",
        "shared"
]

repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    platforms.forEach {
        implementation "com.github.devrawr.commands:${it}:${versionPlatform}"
    }
}
```

## Platform Initialization

```kotlin
fun main()
{
    Platforms
        .usePlatform<BukkitCommandPlatform> { // change this platform to whatever platform you're using 
            this.fallback = "hors"
        }
        .useWrapper<AnnotationCommandWrapper>()
}
```

## Create Command

### Annotation Based Command

#### Parent & sub-command

```kotlin
@Command("name|alias1|alias2")
class TestCommand
{
    @Default // parent command, uses @Command parameter of parent class.
    @CommandPermission("permission")
    fun default(player: Player, amount: Int)
    {
        for (i in 0..amount)
        {
            player.sendMessage("iteration $amount")
        }
    }

    @Command("subcommand|subcommandalias1") // subcommand of parent command
    @CommandPermission("permission")
    fun subcommand(player: Player, message: Array<String>)
    {
        player.sendMessage(message.joinToString(" "))
    }
}
```

#### Several parent commands

```kotlin
class TestCommand
{
    @Command("name|alias1|alias2") // first parent command
    @CommandPermission("permission")
    fun command1(player: Player, amount: Int)
    {
        for (i in 0..amount)
        {
            player.sendMessage("iteration $amount")
        }
    }

    @Command("command2name|command2alias") // other parent command
    @CommandPermission("permission")
    fun command2(player: Player, message: Array<String>)
    {
        player.sendMessage(message.joinToString(" "))
    }
}
```

### Making command without annotations (untested)

```kotlin
fun main()
{
    Platforms.createCommand(
        name = "name|alias1|alias2",
        arguments = arrayOf(Player::class.java, Array<String>::class.java)
    ) {
        val player = it[0] as Player
        val message = it[1] as Array<String>

        player.sendMessage(message.joinToString(" "))
    }
}
```

### Registering a new argument context
#### Creating new context through the ArgumentContext\<T> class
```kotlin
object PlayerContext : ArgumentContext<Player>
{
    override fun fromString(value: String): Player?
    {
        return Bukkit.getPlayer(value)
    }
}
```

#### Registering and creating context in functional way
```kotlin
fun main()
{
    Contexts
        .useContext<Player, PlayerContext>()
        .createContext { // create a new context, provided body is fromString method.
            ChatColor.valueOf(it)
        }
}
```