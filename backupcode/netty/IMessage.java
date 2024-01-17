package site.hellooo.rpc.server;


/*
 * sofa: protocol contains command contains request contains objectData
 *
 *
 *
 *
 * ver: version for protocol
 * type: request/response/request oneway
 * cmdcode: code for remoting command
 * ver2:version for remoting command
 * requestId: id of request
 * codec: code for codec
 * (req)timeout: request timeout.
 * (resp)respStatus: response status
 * classLen: length of request or response class name
 * headerLen: length of header
 * cotentLen: length of content
 * className
 * header
 * content
 */

// header: version type codec requestId bodyLength     body: content
// version: 1B, type: 1B, codec: 1B, requestId: 4B, bodyLength: 4B, content: byte[bodyLength]
public interface IMessage {
    byte getVersion();
    byte getType();
    byte getCodec();
    int getRequestId();
    int getBodyLength();
    byte[] getContent();
}
