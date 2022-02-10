# Commands

simplistic, work in progress, command framework.

# Usages

## Platforms

| Platform    | Platform Class Name   |
| ----------- | --------------------- |
| bukkit      | BukkitCommandPlatform |
| kord - wip  | KordCommandPlatform   |

| Command Wrapper  | Platform Class Name      |
| ---------------- | ------------------------ |
| annotation-based | AnnotationCommandWrapper |

## Build Script

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

## Locale configuration
Using our own (still work in progress) locale system, we offer configuration for the messages across all programs
ran in the same directory. `commands` will create a new directory called `locales` which will contain
all the locales available. You may edit this file as you wish, an example locale file would look someting like:
```properties
#Thu Feb 10 21:19:54 CET 2022
vararg-argument=...
insufficient-permissions=No permission.
user-not-found=User could not be parsed from provided executor.
does-not-meet-arguments=Usage\: /{label} {arguments}
error-prefix=&c 
required-argument=<{name}>
unable-to-parse-executor=Executor could not be parsed from provided user.
optional-argument=[{name}]
unable-to-parse-argument=Unable to parse argument from "{arg}"
help-entry-per-page=6
help-title=&b=== &eShowing help for &f/{parent} &b===
help-entry=&e{label} {args} &7- {description}
help-footer=&eShowing page &b{page-current} &eout of &b{page-max} &f({results} results)
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

#### Using the automatic help generation
```kotlin
@Command("hey|how|test")
object TestCommand
{
    @Help
    @Default
    @HelpDescription("Show this menu")
    @CommandPermission("hello.test")
    fun test()
    {
        println("called test() body") // this won't print!
    }

    @Command("bye")
    @HelpDescription("Broadcast a message.")
    fun bye(lol: Array<String>)
    {
        Bukkit.broadcastMessage(lol.joinToString(" "))
    }

    @Command("yo")
    @HelpDescription("Broadcast your name.")
    fun yo(player: Player, @Value("1") amount: Int)
    {
        for (i in 0..amount)
        {
            Bukkit.broadcastMessage(player.name)
        }
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