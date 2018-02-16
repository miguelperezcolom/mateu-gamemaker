#!/bin/sh


psql -U postgres -d mateuapp -c "drop schema public cascade;"
psql -U postgres -d mateuapp -c "create schema public;"
