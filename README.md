# FurhatChatGPT

This project is designed for **Furhat**, a social robot that facilitates human-robot interaction through voice and facial expressions powered by ChatGPT o3 API. It was created as part of Perception and Action course, Cognitive Science, 2024-25 at Aarhus University. The task of the robot was to engage in two types of task:
## 1. Collaborative game task - First, Last Game
The robot was taking the last word of the participant to place at the beginning of its answer. The participant was instructed to do the same. The robot had a predefined set of words to end its sentences with.
## 2. Free conversation
The robot prompted the participant to ask about their New Year's resolutions and then the conversation ran freely.

## 🛠 Project Overview
This repository contains a structured interaction system for Furhat, including:
- **Custom interaction states** (e.g., greeting, idle, and conversation logic)
- **Parameterized settings** for speech, timing, and flow adjustments
- **State inheritance** for modular and reusable interaction design
- **Idle behavior** to make interactions feel more natural

## 📁 Repository Structure
📂 FurhatGPT/
│── 📄 README.md
│── 📄 main.kt
│── 📄 interactionParams.kt
│── 📄 parent.kt
│── 📄 init.kt
│── 📄 idle.kt
│── 📄 greeting.kt
