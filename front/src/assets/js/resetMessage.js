//resetMessage.js
/**重置message，防止重复点击重复弹出message消息提示 */
import { Message } from "element-ui";

let messageInstance = null;

const resetMessage = (options) => {
  if (messageInstance) {
    messageInstance.close();
  }
  Message.closeAll(); //手动关闭所有消息提示实例
  messageInstance = Message(options);
  // console.log(Message(options));
};

["error", "success", "info", "warning"].forEach((type) => {
  resetMessage[type] = (options) => {
    if (typeof options === "string") {
      options = {
        message: options,
      };
    }
    options.type = type;
    return resetMessage(options);
  };
});

export const message = resetMessage;
