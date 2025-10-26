## Directory Structure

It should implement MVVM, but i'm too lazy to do all that so i modify a lil bit.

- `./repository/` is like `./data/repository` but since it should have `./data/model` and
`./data/source` and i'm lazy so i decided to remove `./data/model` and `./data/source`. And because of that
now `./data` only have `./data/repository` so i decided to move it up and remove `./data`.
- i also remove the `./domain/repository` and replace it with `./contract` because i think its more
suitable lol
- i also move the `./domain/usecase` and `./domain/model` cause why not lol
- `./domain/model` will look like in database (firestore)
