# Livebox Bypass CLI

![GitHub License](https://img.shields.io/github/license/Hequin0x/livebox-bypass-cli)
![GitHub Release](https://img.shields.io/github/v/release/Hequin0x/livebox-bypass-cli)

## Table of contents

- [What is Livebox Bypass CLI?](#what-is-livebox-bypass-cli)
- [How it works](#how-it-works)
- [Getting started](#getting-started)
    - [Available platforms](#available-platforms)
- [Usage](#usage)
    - [Authentication](#authentication)
    - [Examples](#examples)
- [Commands](#commands)

## What is Livebox Bypass CLI?

A command-line tool to generate the required DHCP and GPON/XGS-PON configurations for bypassing Orange Livebox.

## How it works
This tool uses the Livebox API to retrieve the necessary information and outputs ready-to-use DHCP (v4/v6) and GPON settings for third-party routers.

## Getting started

- Download the latest release from the [releases page](https://github.com/Hequin0x/livebox-bypass-cli/releases).

### Available platforms

A native build of the tool is available for the following platforms:

- **Linux** (x86_64 / arm64)
- **macOS** (x86_64 / arm64 (Silicon))
- **Windows** (x86_64)

Note: A non-native (.jar) build is also available, of course you will need to have Java installed on your system if you want to use it.

## Usage

General syntax:
- `./livebox-bypass-cli <command> [options]`

### Authentication

Some commands require authenticating with your Livebox admin password to retrieve necessary information.

You can provide credentials using:
- Interactive prompt (**recommended**): `./livebox-bypass-cli <command> -p`
- Command line (less secure): `./livebox-bypass-cli <command> -p <your_admin_password>`

**Security Note**: Prefer `-p` without a password argument to avoid exposing your password in shell history or process lists.

### Examples

- Interactive password prompt:
    - `./livebox-bypass-cli generate-dhcp -p`
    - `./livebox-bypass-cli generate-gpon -p`

- Non-interactive (not recommended):
    - `./livebox-bypass-cli generate-dhcp -p yourAdminPassword`

## Commands

- `generate-dhcp`
    - Generates DHCPv4 and DHCPv6 configurations for bypassing the Livebox.
        - Requires authentication.
        - Output:
            - DHCPv4 options
                - **60** [Vendor class](https://datatracker.ietf.org/doc/html/rfc2132#section-9.13)
                - **61** [Client identifier](https://datatracker.ietf.org/doc/html/rfc2132#section-9.14)
                - **77** [User class](https://datatracker.ietf.org/doc/html/rfc3004/#section-4)
                - **90** [Authentication](https://datatracker.ietf.org/doc/html/rfc3118#section-2)
            - DHCPv6 options
                - **16** [Vendor class](https://datatracker.ietf.org/doc/html/rfc8415#section-21.16)
                - **1** [Client identifier](https://datatracker.ietf.org/doc/html/rfc8415#section-21.2)
                - **15** [User class](https://datatracker.ietf.org/doc/html/rfc8415#section-21.15)
                - **11** [Authentication](https://datatracker.ietf.org/doc/html/rfc8415#section-21.11)


- `generate-gpon`
    - Generates GPON configurations for bypassing the Livebox.
        - Requires authentication.
        - Note: **GPON** options are also valid for **XGS-PON**
        - Output:
            - **Serial number**
            - **Hardware version**
            - **Vendor ID**


- `wan-link-type`
    - Displays the current WAN link type.
        - Does not require authentication.
        - Output: `gpon` or `xgs-pon`
