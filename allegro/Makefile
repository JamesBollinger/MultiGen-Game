CC=gcc
CFLAGS=-Wall -g -O1
LIBS=-lallegro_image -lallegro_dialog -lallegro

all: keyboard # allegro_test allegro-display game-loop keyboard
	@echo "Cleaning up all object files..."
	@rm -f *.o
	@echo "Compiled the Allegro test."

clean:
	@rm -f *.o

# 
# The following are targets leftover from a different directory
# You may disregard them for now (hence, commented).
#
#allegro_test: allegro_test.o
#	$(CC) -o allegro_test allegro_test.o $(CFLAGS) $(LIBS)
#
#allegro_test.o: allegro_test.c
#	$(CC) -c -o allegro_test.o -Wall allegro_test.c $(CFLAGS) $(LIBS)
#
#allegro-display: allegro-display.o
#	$(CC) -o allegro-display allegro-display.o $(CFLAGS) $(LIBS)
#
#allegro-display.o: allegro-display.c
#	$(CC) -c -o allegro-display.o -Wall allegro-display.c $(CFLAGS) $(LIBS)
#
#game-loop: game-loop.o
#	$(CC) -o game-loop game-loop.o $(CFLAGS) $(LIBS)
#
#game-loop.o: game-loop.c
#	$(CC) -c -o game-loop.o -Wall game-loop.c $(CFLAGS) $(LIBS)
#
keyboard: keyboard.o
	$(CC) -o keyboard-test keyboard.o $(CFLAGS) $(LIBS)

keyboard.o: keyboard.c
	$(CC) -c -o keyboard.o -Wall keyboard.c $(CFLAGS) $(LIBS)

