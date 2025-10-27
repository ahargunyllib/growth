+## Directory Structure

+This project follows a modified MVVM architecture with the following directory organization:

+ `./repository/` consolidates all data repositories. The `./data/` directory was simplified by removing `./data/model` and `./data/source` layers, so `./repository/` was moved to the root level.
+ `./domain/repository` was replaced with `./contract` to better represent the interface contracts for domain operations.
+ `./domain/usecase` and `./domain/model` are organized to reflect the Firestore database structure.