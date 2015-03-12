# mu

Mu is a simple functional programming language I'm working on. The compiler is currently written in Java. There aren't any bells and whistles on the language, it has no static type system, and it's a learning exercise for me above all else so don't expect to be able to use it practically. The compiler is currently in the early stages of development. I plan to have the compiler generate C code.

* The language specification itself is not concrete, and is subject to revision at any time. In fact, the specification doesn't exist - I'm making the language up as I go along.
* The lexical analyser and parser are complete. Parser is a hand-written recursive descent parser with a weird lookahead mechanism.
* Fully resolving identifiers in the AST is almost complete - precompiled library support is not yet implemented.
* Turning functions into closures is complete but not yet tested.
* Generating C output is yet to be done.
* Error reporting is patchy (but it exists!), and stability isn't brilliant.
* Everything is fairly well documented with Javadoc and normal comments - if something is not clear, submit an issue on the GitHub repository.

## syntax

Syntax looks something like this (for linear-time Fibonacci sequence):

    fib <- (\ n ->
             fibR <- (\ n a b ->
                       (? n
                         n\ n=1 -> a;
                         n      -> fibR (b + a) a));
             fibR n 1 1);

Custom operators are also supported:

    (|>) <- (\ a b -> b a);

Currently, user defined operators are all left-associative and the precedence is determined by the first character of the expression. Obvious inspiration is drawn from Haskell and F#, and (to a lesser extent) LISP. The reason for the total lack of language keywords is part brevity and part to write obfuscated code (heh). The language is simple enough, there are only a few defined constructs.

* **Function**, such as `(\ x -> x + 1)`, or `(\ x y -> x * x + y * y |> sqrt)`
* **Binding**, such as `myNumber <- x + 1;` or `vectorLength <- (\ x y -> x * x + y * y |> sqrt)`
* **Pattern-matching**, as in the fibonacci example above - it's the block delimited by `(?` and `)`.
* **Function application**, obviously.
* **Modules**, which is essentially a bunch of bindings delimited by `(@` and `)` which can be nested to form namespaces.
* **Literals**, ie. strings and integers. No floating point support yet, because I'm lazy.
* **Sequences**, which is like a `list` in F# - `[3, 2, -1, 92]`; you know the drill.
* **Tuples**, with items separated by commas. Compiler supports tagged tuples (ie. to form a rudimentary tagged-union system) but I've not decided how to implement this in the language yet.

That's it. Record structures might come soon, as curly-braces aren't used yet. Some things have incomplete support for now, as I attempt to get the language off the ground. For example, pattern-matching, sequences and tuples aren't yet implemented in the compiler. They're coming soon, though, so don't worry!