# gauge-ts-plugin

![Build](https://github.com/Glider2355/gauge-ts-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/24971-gaugets.svg)](https://plugins.jetbrains.com/plugin/24971-gaugets)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/24971-gaugets.svg)](https://plugins.jetbrains.com/plugin/24971-gaugets)

<!-- Plugin description -->
Plugin for writing Gauge tests (originally developed by Thoughtworks Inc.). Supports only steps written in TypeScript.  

## Features
- Auto completion
- Run tests
- Code jumping

## Disable Features
- Debugging
- Find Usages

## Usages
You need to specify the folder where the Step is implemented in the settings screen.

### Settings

![Settings Screenshot](https://raw.githubusercontent.com/Glider2355/gauge-ts-plugin/main/img/img.png)

- Gauge Binary Path
  - Path to the gauge binary
- GAUGE_HOME
  - Path to the gauge home directory
- Max Parallel Nodes
  - Maximum number of parallel nodes to run tests
- Gauge Step Directories
  - Set the directory of the step you want to autocomplete

### How to run the test
- Run Test Gutter Icon

![Run Test Screenshot](https://raw.githubusercontent.com/Glider2355/gauge-ts-plugin/main/img/img2.png)

- Run tests in parallel
  - Specify the directory and execute

![Run Parallel Tests Screenshot](https://raw.githubusercontent.com/Glider2355/gauge-ts-plugin/main/img/img3.png)

<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "gauge-ts-plugin"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/Glider2355/gauge-ts-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
