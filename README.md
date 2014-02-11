hanasu
======

Chat client using homebrew AES encryption module.

Hanasu uses multiple clients, which commicate with one another via a central server. The user enters a message via the vlient interface, and presses send (or the return key). A packet will be generated, and the client will encrypt the data before sending it to the server, using a homebrew AES module. The server will relay the data to the recipient client, where the data will be decrypted, and displayed to the receiving user.
