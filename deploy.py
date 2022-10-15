import os

from slack_sdk import WebClient


def main():
    androidSlackBotToken = os.environ['ANDROID_SLACK_BOT_TOKEN']
    androidSlackChannelKey = "C045WAKTQ6Q"
    client = WebClient(token=androidSlackBotToken)
    response = client.files_upload_v2(
        file="app/build/outputs/apk/debug/app-debug.apk", 
        title="Wonjoong test",
        channel=androidSlackChannelKey,
        initial_comment="Wonjoong is testing right now",
    )
    response.get("file")


if __name__ == "__main__":
    main()
