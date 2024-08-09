package com.robot.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * LEAVING
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: robotService.proto")
public final class LeavingServiceGrpc {

  private LeavingServiceGrpc() {}

  public static final String SERVICE_NAME = "com.robot.grpc.LeavingService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.robot.grpc.RobotService.LeavingRequest,
      com.robot.grpc.RobotService.LeavingResponse> getLeaveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Leave",
      requestType = com.robot.grpc.RobotService.LeavingRequest.class,
      responseType = com.robot.grpc.RobotService.LeavingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.robot.grpc.RobotService.LeavingRequest,
      com.robot.grpc.RobotService.LeavingResponse> getLeaveMethod() {
    io.grpc.MethodDescriptor<com.robot.grpc.RobotService.LeavingRequest, com.robot.grpc.RobotService.LeavingResponse> getLeaveMethod;
    if ((getLeaveMethod = LeavingServiceGrpc.getLeaveMethod) == null) {
      synchronized (LeavingServiceGrpc.class) {
        if ((getLeaveMethod = LeavingServiceGrpc.getLeaveMethod) == null) {
          LeavingServiceGrpc.getLeaveMethod = getLeaveMethod =
              io.grpc.MethodDescriptor.<com.robot.grpc.RobotService.LeavingRequest, com.robot.grpc.RobotService.LeavingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Leave"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.LeavingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.robot.grpc.RobotService.LeavingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new LeavingServiceMethodDescriptorSupplier("Leave"))
              .build();
        }
      }
    }
    return getLeaveMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static LeavingServiceStub newStub(io.grpc.Channel channel) {
    return new LeavingServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static LeavingServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new LeavingServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static LeavingServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new LeavingServiceFutureStub(channel);
  }

  /**
   * <pre>
   * LEAVING
   * </pre>
   */
  public static abstract class LeavingServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void leave(com.robot.grpc.RobotService.LeavingRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.LeavingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getLeaveMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getLeaveMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.robot.grpc.RobotService.LeavingRequest,
                com.robot.grpc.RobotService.LeavingResponse>(
                  this, METHODID_LEAVE)))
          .build();
    }
  }

  /**
   * <pre>
   * LEAVING
   * </pre>
   */
  public static final class LeavingServiceStub extends io.grpc.stub.AbstractStub<LeavingServiceStub> {
    private LeavingServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LeavingServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LeavingServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LeavingServiceStub(channel, callOptions);
    }

    /**
     */
    public void leave(com.robot.grpc.RobotService.LeavingRequest request,
        io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.LeavingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getLeaveMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * LEAVING
   * </pre>
   */
  public static final class LeavingServiceBlockingStub extends io.grpc.stub.AbstractStub<LeavingServiceBlockingStub> {
    private LeavingServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LeavingServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LeavingServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LeavingServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.robot.grpc.RobotService.LeavingResponse leave(com.robot.grpc.RobotService.LeavingRequest request) {
      return blockingUnaryCall(
          getChannel(), getLeaveMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * LEAVING
   * </pre>
   */
  public static final class LeavingServiceFutureStub extends io.grpc.stub.AbstractStub<LeavingServiceFutureStub> {
    private LeavingServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private LeavingServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected LeavingServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new LeavingServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.robot.grpc.RobotService.LeavingResponse> leave(
        com.robot.grpc.RobotService.LeavingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getLeaveMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LEAVE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final LeavingServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(LeavingServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LEAVE:
          serviceImpl.leave((com.robot.grpc.RobotService.LeavingRequest) request,
              (io.grpc.stub.StreamObserver<com.robot.grpc.RobotService.LeavingResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class LeavingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    LeavingServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.robot.grpc.RobotService.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("LeavingService");
    }
  }

  private static final class LeavingServiceFileDescriptorSupplier
      extends LeavingServiceBaseDescriptorSupplier {
    LeavingServiceFileDescriptorSupplier() {}
  }

  private static final class LeavingServiceMethodDescriptorSupplier
      extends LeavingServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    LeavingServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (LeavingServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new LeavingServiceFileDescriptorSupplier())
              .addMethod(getLeaveMethod())
              .build();
        }
      }
    }
    return result;
  }
}
