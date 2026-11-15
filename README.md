<div align="center">

# Multi-Agent System — Moving Blocks

**A Java simulation of agents coordinating their movement around a shared target.**

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)
![Concept](https://img.shields.io/badge/concepts-concurrency%20%7C%20multi--agent-6f42c1)
![Status](https://img.shields.io/badge/status-study%20prototype-lightgrey)

</div>

## Overview

This project implements a small multi-agent simulation in Java using threads. Blue agents move in relation to an orange target while respecting minimum-distance constraints.

It is a compact environment for exploring concurrent execution and coordination rules between multiple moving entities.

## Agent behavior

- Blue blocks follow the orange target.
- A minimum distance is maintained between each blue block and the target.
- A minimum distance is maintained between blue blocks themselves.

These rules define the interaction model documented for the project.

## Technology stack

- Java
- Threads for concurrent agent execution

## Getting started

### Requirements

- Java Development Kit (JDK)
- A terminal environment with `javac` and `java`

### Build

The repository includes a helper script:

```bash
chmod +x comp.sh
./comp.sh
```

The script contains the original compilation procedure. Review it if your JDK or directory layout differs.

### Run

Execute the class produced by the build script. The exact class name and launch command depend on the source files under `src/`.

## Repository layout

```text
.
├── src/
├── comp.sh
└── README.md
```

## Concepts explored

- Multi-agent systems
- Concurrency and threads
- Target-following behavior
- Distance constraints
- Coordinated movement

## Project status

Educational simulation / prototype. Formal performance and scalability testing are not documented.

## License

No license file was identified in the repository at the time of writing. Contact the author before redistributing or reusing the project.
