#!/bin/sh
# ====================================================================================================
# データベース接続先情報
# ====================================================================================================
HOST=127.0.0.1
PORT=1521
SID=XE
DATABASE_URL=jdbc:oracle:thin:@${HOST}:${PORT}:${SID}
# ====================================================================================================
# ユーザ、パスワード情報
# ====================================================================================================
USER=scott
PASSWORD=tigger
