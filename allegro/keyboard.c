#include <allegro5/allegro.h>
#include <allegro5/allegro_image.h>
#include <allegro5/allegro_native_dialog.h>
#include <stdio.h>
/*#include <time.h>
#include <unistd.h>*/

enum KEYSUSED {
    /* */
    KEY_ESC, KEY_Q, KEY_E, KEY_P, KEY_UP, KEY_DOWN, KEY_LEFT, KEY_RIGHT, 
    KEY_ENTER
};

int main(int argc, char **argv) {
    printf("Testing printf for debugging (start of main()... ~line 15)\n");
    ALLEGRO_DISPLAY *display = NULL;
/*    ALLEGRO_BITMAP *img = NULL;*/
    int result = al_init();
/*    int game_state = 1;*/
/*    unsigned int *time = (unsigned int *) malloc(sieof(unsigned int));*/
/*    *time = 0;*/
    if (result == 0) {
        fprintf(stderr, "Failed to start Allegro game engine!\n");
        al_show_native_message_box(display, "error", "error", "Failed to initialize Allegro game engine",
            NULL, ALLEGRO_MESSAGEBOX_ERROR);
        return -1;
    }
    
    result = al_init_image_addon();
    printf("Testing printf for debugging (init_image... ~line 30)\n");
    if (result == 0) {
        fprintf(stderr, "Failed to start Allegro img  addon!\n");
        al_show_native_message_box(display, "error", "error", "Failed to initialize Allegro img addon",
            NULL, ALLEGRO_MESSAGEBOX_ERROR);
        return -1;
    }
    
    display = al_create_display(1000, 800);
    if (display == NULL) {
        fprintf(stderr, "Failed to start Allegro display struct!\n");
        al_show_native_message_box(display, "error", "error", "Failed to start the display structure",
            NULL, ALLEGRO_MESSAGEBOX_ERROR);
        return -1;
    }
    
    ALLEGRO_EVENT_QUEUE *decisions_response_queue = NULL;
    ALLEGRO_EVENT next_ev;
    decisions_response_queue = al_create_event_queue();

    int gameActive = 1;
    int paused = 0;

    result = al_install_keyboard();
    if (result == 0) {
        fprintf(stderr, "Failed to start Allegro display struct!\n");
        al_show_native_message_box(display, "error", "error", "Failed to start the display structure",
            NULL, ALLEGRO_MESSAGEBOX_ERROR);
        al_destroy_display(display);
        return -1;
    }

    al_register_event_source(decisions_response_queue, al_get_display_event_source(display));
    al_register_event_source(decisions_response_queue, al_get_keyboard_event_source());

    printf("Testing printf for debugging (between registering events and image array... ~line 66)\n");
    int ind, curImgInd;
    ALLEGRO_BITMAP *curImg;
    ALLEGRO_BITMAP *imgs[5];
    for (ind=0; ind < 5; ind ++) {
        char fname[26]="../artwork/nic-cage-0.jpg\0";
        fname[20] = (char)(ind+48);
        imgs[ind] = al_load_bitmap(fname);
    }
    curImgInd = 0;
    curImg = imgs[0];

    while (gameActive) {
        if (paused) {
            al_wait_for_event(decisions_response_queue, &next_ev);
            if (next_ev.type == ALLEGRO_EVENT_KEY_DOWN) {
                if (next_ev.keyboard.keycode == ALLEGRO_KEY_P) {
                    /* */
                    printf("\"P\" Key has been pressed\n");
                    paused = 0;
                }
            }
        } else {
            al_wait_for_event(decisions_response_queue, &next_ev);
            if (next_ev.type == ALLEGRO_EVENT_KEY_DOWN) {
                if (next_ev.keyboard.keycode == ALLEGRO_KEY_P) {
                    /* */
                    printf("\"P\" Key has been pressed\n");
                    paused = 1;
                } else if (next_ev.keyboard.keycode == ALLEGRO_KEY_N) {
                    /* */
                    printf("\"N\" Key has been pressed\n");
                    curImgInd ++;
                    curImgInd = (curImgInd % 5);
                    curImg = imgs[curImgInd];
                } else if (next_ev.keyboard.keycode == ALLEGRO_KEY_Q) {
                    /* */
                    gameActive = 0;
                    printf("\"Q\" Key has been pressed (exiting)\n");
                }
            } else if (next_ev.type == ALLEGRO_EVENT_KEY_UP) {
                printf("Some key has just been released\n");
            } else if (next_ev.type == ALLEGRO_EVENT_DISPLAY_CLOSE) {
                gameActive = 0;
            }/* else if () {
                
            }*/
            al_clear_to_color(al_map_rgb(255, 167, 26));
            al_draw_bitmap(curImg, 10, 10, 0);
            al_flip_display();
/*            al_rest(6000);*/
/*            sleep(6);*/
        }
        /*
        switch () {
            case :
                break;
            case :
                break;
            default:
                break;
        }*/
    }
    /*
    img = al_load_bitmap("res/nic-cage-0.jpg");
    if (img == NULL) {
        fprintf(stderr, "Failed to load the img\n");
        al_show_native_message_box(display, "error", "error", "Failed to find the given image",
            NULL, ALLEGRO_MESSAGEBOX_ERROR);
        al_destroy_display(display);
        return -1;
    }
    *//*
    al_clear_to_color(al_map_rgb(255, 167, 26));
    al_draw_bitmap(img, 100, 100, 0);
    *//*
    al_flip_display();
    al_rest(60);
    */
    al_destroy_bitmap(curImg);
    al_destroy_display(display);
    return 0;
}
